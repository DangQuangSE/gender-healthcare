package com.S_Health.GenderHealthCare.modules.appointment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.scheduling.domain.ConsultantSlot;
import com.S_Health.GenderHealthCare.modules.scheduling.domain.ServiceSlotPool;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.UpdateAppointmentRequest;
import com.S_Health.GenderHealthCare.repository.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import com.S_Health.GenderHealthCare.repository.AuthenticationRepository;
import com.S_Health.GenderHealthCare.repository.ConsultantSlotRepository;
import com.S_Health.GenderHealthCare.repository.ServiceSlotPoolRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final ServiceSlotPoolRepository serviceSlotPoolRepository;
    private final AuthenticationRepository authenticationRepository;
    private final ConsultantSlotRepository consultantSlotRepository;
    private final AuthUtil authUtil;
    private final AppointmentStatusCalculator statusCalculator;
    private final AppointmentQueryService appointmentQueryService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            ServiceSlotPoolRepository serviceSlotPoolRepository,
            AuthenticationRepository authenticationRepository,
            ConsultantSlotRepository consultantSlotRepository,
            AuthUtil authUtil,
            AppointmentStatusCalculator statusCalculator,
            AppointmentQueryService appointmentQueryService) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.serviceSlotPoolRepository = serviceSlotPoolRepository;
        this.authenticationRepository = authenticationRepository;
        this.consultantSlotRepository = consultantSlotRepository;
        this.authUtil = authUtil;
        this.statusCalculator = statusCalculator;
        this.appointmentQueryService = appointmentQueryService;
    }

    @Transactional
    public AppointmentResponse updateAppointment(Long appointmentId, UpdateAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));

        Long userId = authUtil.getCurrentUserId();
        User user = authUtil.getCurrentUser();
        boolean isOwner = userId.equals(appointment.getCustomer().getId());
        boolean isPrivileged = user.getRole() == UserRole.ADMIN
                || user.getRole() == UserRole.STAFF;

        if (!isOwner && !isPrivileged) {
            throw new DomainException(ErrorCode.FORBIDDEN, AppointmentMessages.UPDATE_FORBIDDEN);
        }

        if (request.getSlotId() != null) {
            ServiceSlotPool newSlot = serviceSlotPoolRepository.findById(request.getSlotId())
                    .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.SLOT_NOT_FOUND));

            LocalDateTime newSlotTime = LocalDateTime.of(request.getPreferredDate(), newSlot.getStartTime());
            if (isOwner && newSlotTime.minusDays(1).isBefore(LocalDateTime.now())) {
                throw new DomainException(AppointmentMessages.SLOT_CHANGE_TOO_LATE);
            }

            ServiceSlotPool oldSlot = appointment.getServiceSlotPool();
            oldSlot.setAvailableBooking(oldSlot.getAvailableBooking() + 1);
            oldSlot.setCurrentBooking(oldSlot.getCurrentBooking() - 1);
            newSlot.setAvailableBooking(newSlot.getAvailableBooking() - 1);
            newSlot.setCurrentBooking(newSlot.getCurrentBooking() + 1);

            serviceSlotPoolRepository.save(oldSlot);
            serviceSlotPoolRepository.save(newSlot);
            appointment.setServiceSlotPool(newSlot);
            appointment.setPreferredDate(request.getPreferredDate());
        }

        if (request.getNote() != null) {
            appointment.setNote(request.getNote());
        }

        if (isPrivileged) {
            if (request.getStatus() != null && appointment.getStatus() != request.getStatus()) {
                appointment.setStatus(request.getStatus());
            }
            if (request.getConsultantId() != null) {
                User consultant = authenticationRepository.findById(request.getConsultantId())
                        .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.CONSULTANT_NOT_FOUND));
                appointment.setConsultant(consultant);
            }
            if (request.getPrice() != null) {
                appointment.setPrice(request.getPrice());
            }
        }

        appointmentRepository.save(appointment);
        return appointmentQueryService.getAppointmentById(appointmentId);
    }

    @Transactional
    public void deleteAppointment(long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));
        List<AppointmentDetail> details = appointmentDetailRepository.findByAppointment(appointment);
        details.forEach(detail -> detail.setIsActive(false));
        appointmentDetailRepository.saveAll(details);
        appointment.setIsActive(false);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));

        if (appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new DomainException(ErrorCode.CONFLICT, AppointmentMessages.ALREADY_CANCELED);
        }

        List<AppointmentDetail> details = appointmentDetailRepository
                .findByAppointmentAndIsActiveTrue(appointment);
        for (AppointmentDetail detail : details) {
            detail.setStatus(AppointmentStatus.CANCELED);
            ConsultantSlot consultantSlot = consultantSlotRepository
                    .findByConsultantAndDateAndStartTimeAndIsActiveTrue(
                            detail.getConsultant(),
                            detail.getSlotTime().toLocalDate(),
                            detail.getSlotTime().toLocalTime());

            if (consultantSlot != null) {
                consultantSlot.setCurrentBooking(consultantSlot.getCurrentBooking() - 1);
                consultantSlot.setAvailableBooking(consultantSlot.getAvailableBooking() + 1);
                consultantSlotRepository.save(consultantSlot);
            }
        }
        appointmentDetailRepository.saveAll(details);

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.setUpdate_at(LocalDateTime.now());

        ServiceSlotPool slot = appointment.getServiceSlotPool();
        if (slot != null) {
            slot.setAvailableBooking(slot.getAvailableBooking() + 1);
            slot.setCurrentBooking(slot.getCurrentBooking() - 1);
            serviceSlotPoolRepository.save(slot);
        }
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void checkInAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));
        try {
            appointment.setStatus(AppointmentStatus.CHECKED);
            appointment.setUpdate_at(LocalDateTime.now());

            List<AppointmentDetail> appointmentDetails = appointmentDetailRepository.findByAppointment(appointment);
            for (AppointmentDetail appointmentDetail : appointmentDetails) {
                appointmentDetail.setStatus(AppointmentStatus.CHECKED);
                appointmentDetailRepository.save(appointmentDetail);
            }
            appointmentRepository.save(appointment);
        } catch (Exception exception) {
            throw new DomainException(
                    ErrorCode.INTERNAL_ERROR,
                    AppointmentMessages.APPOINTMENT_STATUS_UPDATE_FAILED,
                    exception);
        }
    }

    @Transactional
    public void updateAppointmentDetailStatus(Long detailId, AppointmentStatus status) {
        AppointmentDetail detail = appointmentDetailRepository.findById(detailId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_DETAIL_NOT_FOUND));

        User currentUser = authUtil.getCurrentUser();
        if (detail.getConsultant().getId() != currentUser.getId()) {
            throw new DomainException(ErrorCode.FORBIDDEN, AppointmentMessages.DETAIL_UPDATE_FORBIDDEN);
        }

        if (status != AppointmentStatus.IN_PROGRESS
                && status != AppointmentStatus.WAITING_RESULT
                && status != AppointmentStatus.COMPLETED) {
            throw new DomainException(AppointmentMessages.DETAIL_STATUS_INVALID);
        }

        try {
            detail.setStatus(status);
            detail.setUpdate_at(LocalDateTime.now());
            appointmentDetailRepository.save(detail);

            Appointment appointment = detail.getAppointment();
            List<AppointmentDetail> allDetails = appointmentDetailRepository
                    .findByAppointmentAndIsActiveTrue(appointment);
            AppointmentStatus newAppointmentStatus = statusCalculator.calculateStatus(allDetails);

            if (appointment.getStatus() != newAppointmentStatus) {
                appointment.setStatus(newAppointmentStatus);
                appointment.setUpdate_at(LocalDateTime.now());
                appointmentRepository.save(appointment);
            }
        } catch (Exception exception) {
            throw new DomainException(ErrorCode.INTERNAL_ERROR, AppointmentMessages.STATUS_UPDATE_FAILED, exception);
        }
    }

    @Transactional
    public void updateIsRated(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));
        appointment.setIsRated(true);
        appointmentRepository.save(appointment);
    }
}
