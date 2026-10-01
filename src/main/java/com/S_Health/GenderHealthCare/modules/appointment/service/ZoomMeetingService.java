package com.S_Health.GenderHealthCare.modules.appointment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;
import com.S_Health.GenderHealthCare.integrations.mail.EmailService;
import com.S_Health.GenderHealthCare.integrations.zoom.ZoomMeetingClient;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.catalog.enums.ServiceType;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * Appointment use case for creating the online consultation meeting.
 */
@Service
public class ZoomMeetingService {
    private static final ZoneId APPLICATION_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter ZOOM_START_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private final ZoomMeetingClient zoomMeetingClient;
    private final AppointmentRepository appointmentRepository;
    private final AuthUtil authUtil;
    private final EmailService emailService;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final PaymentRepository paymentRepository;

    public ZoomMeetingService(
            ZoomMeetingClient zoomMeetingClient,
            AppointmentRepository appointmentRepository,
            AuthUtil authUtil,
            EmailService emailService,
            AppointmentDetailRepository appointmentDetailRepository,
            PaymentRepository paymentRepository) {
        this.zoomMeetingClient = zoomMeetingClient;
        this.appointmentRepository = appointmentRepository;
        this.authUtil = authUtil;
        this.emailService = emailService;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Map<String, String> createMeeting(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.NOT_FOUND,
                        IntegrationMessages.ZOOM_APPOINTMENT_NOT_FOUND));

        if (appointment.getCustomer() == null
                || !Objects.equals(authUtil.getCurrentUserId(), appointment.getCustomer().getId())) {
            throw new DomainException(
                    ErrorCode.FORBIDDEN,
                    IntegrationMessages.ZOOM_USER_NOT_IN_MEETING);
        }

        AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentIdForUpdate(appointmentId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.NOT_FOUND,
                        IntegrationMessages.ZOOM_APPOINTMENT_DETAIL_NOT_FOUND));

        if (!Boolean.TRUE.equals(appointment.getIsActive())
                || !Boolean.TRUE.equals(appointmentDetail.getIsActive())
                || appointment.getStatus() != AppointmentStatus.CONFIRMED
                || appointmentDetail.getStatus() != AppointmentStatus.CONFIRMED
                || appointment.getService() == null
                || appointment.getService().getType() != ServiceType.CONSULTING_ON) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    IntegrationMessages.ZOOM_APPOINTMENT_INVALID);
        }

        if (paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.SUCCESS).isEmpty()) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    IntegrationMessages.ZOOM_PAYMENT_REQUIRED);
        }

        boolean hasJoinUrl = hasText(appointmentDetail.getJoinUrl());
        boolean hasStartUrl = hasText(appointmentDetail.getStartUrl());
        if (hasJoinUrl && hasStartUrl) {
            return Map.of(
                    "join_url", appointmentDetail.getJoinUrl(),
                    "start_url", appointmentDetail.getStartUrl());
        }
        if (hasJoinUrl || hasStartUrl) {
            throw new DomainException(
                    ErrorCode.CONFLICT,
                    IntegrationMessages.ZOOM_MEETING_LINKS_INCOMPLETE);
        }

        if (appointmentDetail.getSlotTime() == null) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    IntegrationMessages.ZOOM_SLOT_TIME_MISSING);
        }

        String startTime = appointmentDetail.getSlotTime()
                .atZone(APPLICATION_ZONE)
                .format(ZOOM_START_TIME_FORMATTER);
        Map<String, String> meetingLinks = zoomMeetingClient.createMeeting(
                appointment.getService().getName(),
                startTime);

        String joinUrl = meetingLinks == null ? null : meetingLinks.get("join_url");
        String startUrl = meetingLinks == null ? null : meetingLinks.get("start_url");
        if (!hasText(joinUrl) || !hasText(startUrl)) {
            throw new DomainException(
                    ErrorCode.INTEGRATION_ERROR,
                    IntegrationMessages.ZOOM_MEETING_LINKS_INCOMPLETE);
        }
        appointmentDetail.setJoinUrl(joinUrl);
        appointmentDetail.setStartUrl(startUrl);
        appointmentDetailRepository.save(appointmentDetail);

        String serviceName = appointment.getService().getName();
        emailService.sendUrlCurtomerZoom(
                appointment.getCustomer().getEmail(),
                startTime,
                joinUrl,
                serviceName);
        emailService.sendUrlConsultantZoom(
                appointmentDetail.getConsultant().getEmail(),
                startTime,
                startUrl,
                serviceName);

        return meetingLinks;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
