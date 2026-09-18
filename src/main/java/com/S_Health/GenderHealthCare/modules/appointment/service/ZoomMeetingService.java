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
import com.S_Health.GenderHealthCare.repository.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Appointment use case for creating the online consultation meeting.
 */
@Service
public class ZoomMeetingService {
    private final ZoomMeetingClient zoomMeetingClient;
    private final AppointmentRepository appointmentRepository;
    private final AuthUtil authUtil;
    private final EmailService emailService;
    private final AppointmentDetailRepository appointmentDetailRepository;

    public ZoomMeetingService(
            ZoomMeetingClient zoomMeetingClient,
            AppointmentRepository appointmentRepository,
            AuthUtil authUtil,
            EmailService emailService,
            AppointmentDetailRepository appointmentDetailRepository) {
        this.zoomMeetingClient = zoomMeetingClient;
        this.appointmentRepository = appointmentRepository;
        this.authUtil = authUtil;
        this.emailService = emailService;
        this.appointmentDetailRepository = appointmentDetailRepository;
    }

    public Map<String, String> createMeeting(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.NOT_FOUND,
                        IntegrationMessages.ZOOM_APPOINTMENT_NOT_FOUND));

        AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.NOT_FOUND,
                        IntegrationMessages.ZOOM_APPOINTMENT_DETAIL_NOT_FOUND));

        if (appointmentDetail.getStartUrl() != null || appointmentDetail.getJoinUrl() != null) {
            throw new DomainException(
                    ErrorCode.CONFLICT,
                    IntegrationMessages.ZOOM_MEETING_ALREADY_EXISTS);
        }

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED
                || appointment.getService().getType() != ServiceType.CONSULTING_ON) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    IntegrationMessages.ZOOM_APPOINTMENT_INVALID);
        }

        if (!Objects.equals(authUtil.getCurrentUserId(), appointment.getCustomer().getId())) {
            throw new DomainException(
                    ErrorCode.FORBIDDEN,
                    IntegrationMessages.ZOOM_USER_NOT_IN_MEETING);
        }

        String startTime = appointment.getServiceSlotPool().getStartTime().toString();
        Map<String, String> meetingLinks = zoomMeetingClient.createMeeting(
                appointment.getService().getName(),
                startTime);

        String joinUrl = meetingLinks.get("join_url");
        String startUrl = meetingLinks.get("start_url");
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

}
