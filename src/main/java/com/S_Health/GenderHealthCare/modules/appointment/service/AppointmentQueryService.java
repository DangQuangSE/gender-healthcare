package com.S_Health.GenderHealthCare.modules.appointment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentScheduleQuery;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDetailResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.PatientHistoryResponse;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomResponse;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalResult;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.BasicMedicalProfileResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalProfileResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalResultResponse;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalProfileRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalResultRepository;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final MedicalResultRepository medicalResultRepository;
    private final MedicalProfileRepository medicalProfileRepository;
    private final ModelMapper modelMapper;
    private final CurrentUserProvider currentUserProvider;

    public AppointmentQueryService(
            AppointmentRepository appointmentRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            MedicalResultRepository medicalResultRepository,
            MedicalProfileRepository medicalProfileRepository,
            ModelMapper modelMapper,
            CurrentUserProvider currentUserProvider) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.medicalResultRepository = medicalResultRepository;
        this.medicalProfileRepository = medicalProfileRepository;
        this.modelMapper = modelMapper;
        this.currentUserProvider = currentUserProvider;
    }

    public AppointmentResponse getAppointmentById(long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));
        List<AppointmentDetail> details = appointmentDetailRepository
                .findByAppointmentAndIsActiveTrue(appointment);
        return mapAppointments(List.of(appointment), Map.of(appointment, details), true).get(0);
    }

    public PatientHistoryResponse getPatientHistoryFromAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));

        MedicalProfile medicalProfile = appointment.getMedicalProfile();
        if (medicalProfile == null) {
            throw new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.MEDICAL_PROFILE_NOT_FOUND);
        }

        List<Appointment> pastAppointments = appointmentRepository
                .findByMedicalProfileAndStatusAndIsActiveTrue(medicalProfile, AppointmentStatus.COMPLETED);

        PatientHistoryResponse historyDto = new PatientHistoryResponse();
        historyDto.setMedicalProfile(modelMapper.map(medicalProfile, MedicalProfileResponse.class));
        historyDto.setPastAppointments(convertToDto(pastAppointments));
        return historyDto;
    }

    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        User currentUser = currentUserProvider.requireUser();
        List<Appointment> appointments = currentUser.getRole() == UserRole.CUSTOMER
                ? appointmentRepository.findByCustomerAndStatusAndIsActiveTrue(currentUser, status)
                : appointmentRepository.findByStatusAndIsActiveTrue(status);
        return convertToDto(appointments);
    }

    public List<AppointmentResponse> getAppointmentsForConsultantOnDateByDetailStatus(
            AppointmentScheduleQuery request) {
        LocalDate date = request.getDate();
        AppointmentStatus detailStatus = request.getStatus();
        User currentDoctor = currentUserProvider.requireUser();

        List<AppointmentDetail> filteredDetails = detailStatus != null
                ? appointmentDetailRepository.findByConsultant_idAndSlotDateAndStatus(
                currentDoctor.getId(), date, detailStatus)
                : appointmentDetailRepository.findByConsultant_idAndSlotDate(
                currentDoctor.getId(), date);

        Map<Appointment, List<AppointmentDetail>> detailsByAppointment = filteredDetails.stream()
                .filter(detail -> detail.getAppointment().getIsActive())
                .collect(Collectors.groupingBy(AppointmentDetail::getAppointment));

        return mapAppointments(
                List.copyOf(detailsByAppointment.keySet()),
                detailsByAppointment,
                false
        );
    }

    private List<AppointmentResponse> convertToDto(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            return Collections.emptyList();
        }

        List<AppointmentDetail> details = appointmentDetailRepository
                .findByAppointmentInAndIsActiveTrue(appointments);
        Map<Appointment, List<AppointmentDetail>> detailsByAppointment = details.stream()
                .collect(Collectors.groupingBy(AppointmentDetail::getAppointment));
        return mapAppointments(appointments, detailsByAppointment, false);
    }

    private List<AppointmentResponse> mapAppointments(
            List<Appointment> appointments,
            Map<Appointment, List<AppointmentDetail>> detailsByAppointment,
            boolean requireMedicalResult) {
        if (appointments.isEmpty()) {
            return Collections.emptyList();
        }

        List<AppointmentDetail> allDetails = detailsByAppointment.values().stream()
                .flatMap(List::stream)
                .toList();
        Map<Long, MedicalResult> resultsByDetailId = allDetails.isEmpty()
                ? Collections.emptyMap()
                : medicalResultRepository.findByAppointmentDetailIn(allDetails).stream()
                .collect(Collectors.toMap(
                        result -> result.getAppointmentDetail().getId(),
                        Function.identity(),
                        (first, ignored) -> first
                ));
        Map<Long, BasicMedicalProfileResponse> profilesByCustomerId = loadBasicMedicalProfiles(appointments);

        return appointments.stream()
                .map(appointment -> mapAppointmentWithDetails(
                        appointment,
                        detailsByAppointment.getOrDefault(appointment, Collections.emptyList()),
                        resultsByDetailId,
                        profilesByCustomerId,
                        requireMedicalResult
                ))
                .toList();
    }

    private AppointmentResponse mapAppointmentWithDetails(
            Appointment appointment,
            List<AppointmentDetail> details,
            Map<Long, MedicalResult> resultsByDetailId,
            Map<Long, BasicMedicalProfileResponse> profilesByCustomerId,
            boolean requireMedicalResult) {
        AppointmentResponse appointmentDto = modelMapper.map(appointment, AppointmentResponse.class);
        appointmentDto.setCustomerId(appointment.getCustomer().getId());
        appointmentDto.setCustomerName(appointment.getCustomer().getFullname());
        appointmentDto.setServiceName(appointment.getService().getName());
        appointmentDto.setAppointmentDetails(details.stream()
                .map(detail -> mapDetail(detail, resultsByDetailId, requireMedicalResult))
                .toList());
        appointmentDto.setCustomerMedicalProfile(
                profilesByCustomerId.get(appointment.getCustomer().getId())
        );
        return appointmentDto;
    }

    private AppointmentDetailResponse mapDetail(
            AppointmentDetail detail,
            Map<Long, MedicalResult> resultsByDetailId,
            boolean requireMedicalResult) {
        AppointmentDetailResponse detailDto = modelMapper.map(detail, AppointmentDetailResponse.class);
        detailDto.setConsultantName(detail.getConsultant().getFullname());
        detailDto.setServiceName(detail.getService().getName());
        detailDto.setRoom(mapRoomToSimpleDto(detail.getRoom()));

        MedicalResult medicalResult = resultsByDetailId.get(detail.getId());
        if (requireMedicalResult && medicalResult == null) {
            throw new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.RESULT_NOT_FOUND);
        }
        if (medicalResult != null) {
            detailDto.setMedicalResult(modelMapper.map(medicalResult, MedicalResultResponse.class));
        }
        return detailDto;
    }

    private SimpleRoomResponse mapRoomToSimpleDto(com.S_Health.GenderHealthCare.modules.catalog.domain.Room room) {
        if (room == null) {
            return null;
        }
        SimpleRoomResponse roomDto = modelMapper.map(room, SimpleRoomResponse.class);
        if (room.getSpecialization() != null) {
            roomDto.setSpecializationName(room.getSpecialization().getName());
        }
        return roomDto;
    }

    private Map<Long, BasicMedicalProfileResponse> loadBasicMedicalProfiles(List<Appointment> appointments) {
        List<Long> customerIds = appointments.stream()
                .map(appointment -> appointment.getCustomer().getId())
                .distinct()
                .toList();
        if (customerIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return medicalProfileRepository.findByCustomerIdInAndIsActiveTrue(customerIds).stream()
                .collect(Collectors.groupingBy(profile -> profile.getCustomer().getId()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> selectBasicMedicalProfile(entry.getValue())));
    }

    private BasicMedicalProfileResponse selectBasicMedicalProfile(List<MedicalProfile> profiles) {
        if (profiles.isEmpty()) {
            return null;
        }

        MedicalProfile latestProfile = profiles.stream()
                .filter(profile -> profile.getAllergies() != null
                        || profile.getFamilyHistory() != null
                        || profile.getChronicConditions() != null
                        || profile.getSpecialNotes() != null)
                .max((first, second) -> first.getUpdatedAt().compareTo(second.getUpdatedAt()))
                .orElse(profiles.get(0));
        return modelMapper.map(latestProfile, BasicMedicalProfileResponse.class);
    }
}
