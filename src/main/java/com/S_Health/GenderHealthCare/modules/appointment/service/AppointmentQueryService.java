package com.S_Health.GenderHealthCare.modules.appointment.service;

import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDetailResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.BasicMedicalProfileResponse;
import com.S_Health.GenderHealthCare.modules.appointment.dto.response.PatientHistoryResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalResultResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalProfileResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.dto.request.AppointmentScheduleQuery;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalResult;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.repository.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import com.S_Health.GenderHealthCare.repository.MedicalProfileRepository;
import com.S_Health.GenderHealthCare.repository.MedicalResultRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
public class AppointmentQueryService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final MedicalResultRepository medicalResultRepository;
    private final MedicalProfileRepository medicalProfileRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    public AppointmentQueryService(
            AppointmentRepository appointmentRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            MedicalResultRepository medicalResultRepository,
            MedicalProfileRepository medicalProfileRepository,
            ModelMapper modelMapper,
            AuthUtil authUtil) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.medicalResultRepository = medicalResultRepository;
        this.medicalProfileRepository = medicalProfileRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    public AppointmentResponse getAppointmentById(long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.APPOINTMENT_NOT_FOUND));

        List<AppointmentDetail> appointmentDetails = appointmentDetailRepository
                .findByAppointmentAndIsActiveTrue(appointment);
        List<AppointmentDetailResponse> detailDtos = new ArrayList<>();

        for (AppointmentDetail appointmentDetail : appointmentDetails) {
            MedicalResult medicalResult = medicalResultRepository.findByAppointmentDetail(appointmentDetail)
                    .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, AppointmentMessages.RESULT_NOT_FOUND));

            AppointmentDetailResponse detailDto = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
            detailDto.setConsultantName(appointmentDetail.getConsultant().getFullname());
            detailDto.setServiceName(appointmentDetail.getService().getName());
            detailDto.setMedicalResult(modelMapper.map(medicalResult, MedicalResultResponse.class));
            detailDto.setRoom(mapRoomToSimpleDto(appointmentDetail.getRoom()));
            detailDtos.add(detailDto);
        }

        AppointmentResponse appointmentDto = modelMapper.map(appointment, AppointmentResponse.class);
        appointmentDto.setCustomerId(appointment.getCustomer().getId());
        appointmentDto.setCustomerName(appointment.getCustomer().getFullname());
        appointmentDto.setServiceName(appointment.getService().getName());
        appointmentDto.setAppointmentDetails(detailDtos);
        appointmentDto.setCustomerMedicalProfile(getBasicMedicalProfile(appointment.getCustomer().getId()));
        return appointmentDto;
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
        List<AppointmentResponse> pastAppointmentDtos = pastAppointments.stream()
                .map(pastAppointment -> getAppointmentById(pastAppointment.getId()))
                .collect(Collectors.toList());

        PatientHistoryResponse historyDto = new PatientHistoryResponse();
        historyDto.setMedicalProfile(modelMapper.map(medicalProfile, MedicalProfileResponse.class));
        historyDto.setPastAppointments(pastAppointmentDtos);
        return historyDto;
    }

    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus status) {
        User currentUser = authUtil.getCurrentUser();
        List<Appointment> appointments;

        if (currentUser.getRole() == UserRole.CUSTOMER) {
            appointments = appointmentRepository
                    .findByCustomerAndStatusAndIsActiveTrue(currentUser, status);
        } else {
            appointments = appointmentRepository.findByStatusAndIsActiveTrue(status);
        }

        return convertToDto(appointments);
    }

    public List<AppointmentResponse> getAppointmentsForConsultantOnDateByDetailStatus(
            AppointmentScheduleQuery request) {
        LocalDate date = request.getDate();
        AppointmentStatus detailStatus = request.getStatus();
        User currentDoctor = authUtil.getCurrentUser();

        List<AppointmentDetail> filteredDetails;
        if (detailStatus != null) {
            filteredDetails = appointmentDetailRepository
                    .findByConsultant_idAndSlotDateAndStatus(currentDoctor.getId(), date, detailStatus);
        } else {
            filteredDetails = appointmentDetailRepository
                    .findByConsultant_idAndSlotDate(currentDoctor.getId(), date);
        }

        Map<Appointment, List<AppointmentDetail>> appointmentDetailsMap = filteredDetails.stream()
                .filter(detail -> detail.getAppointment().getIsActive())
                .collect(Collectors.groupingBy(AppointmentDetail::getAppointment));

        return appointmentDetailsMap.entrySet().stream()
                .map(entry -> mapAppointmentWithDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<AppointmentResponse> convertToDto(List<Appointment> appointments) {
        return appointments.stream()
                .map(appointment -> {
                    List<AppointmentDetail> details = appointmentDetailRepository
                            .findByAppointmentAndIsActiveTrue(appointment);
                    return mapAppointmentWithDetails(appointment, details);
                })
                .collect(Collectors.toList());
    }

    private AppointmentResponse mapAppointmentWithDetails(
            Appointment appointment,
            List<AppointmentDetail> details) {
        AppointmentResponse appointmentDto = modelMapper.map(appointment, AppointmentResponse.class);
        appointmentDto.setCustomerId(appointment.getCustomer().getId());
        appointmentDto.setCustomerName(appointment.getCustomer().getFullname());
        appointmentDto.setServiceName(appointment.getService().getName());

        List<AppointmentDetailResponse> detailDtos = details.stream()
                .map(this::mapDetail)
                .collect(Collectors.toList());
        appointmentDto.setAppointmentDetails(detailDtos);
        appointmentDto.setCustomerMedicalProfile(getBasicMedicalProfile(appointment.getCustomer().getId()));
        return appointmentDto;
    }

    private AppointmentDetailResponse mapDetail(AppointmentDetail detail) {
        AppointmentDetailResponse detailDto = modelMapper.map(detail, AppointmentDetailResponse.class);
        detailDto.setConsultantName(detail.getConsultant().getFullname());
        detailDto.setServiceName(detail.getService().getName());
        detailDto.setRoom(mapRoomToSimpleDto(detail.getRoom()));
        medicalResultRepository.findByAppointmentDetail(detail)
                .ifPresent(result -> detailDto.setMedicalResult(modelMapper.map(result, MedicalResultResponse.class)));
        return detailDto;
    }

    private com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomResponse mapRoomToSimpleDto(
            com.S_Health.GenderHealthCare.modules.catalog.domain.Room room) {
        if (room == null) {
            return null;
        }

        com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomResponse roomDto =
                modelMapper.map(room, com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomResponse.class);
        if (room.getSpecialization() != null) {
            roomDto.setSpecializationName(room.getSpecialization().getName());
        }
        return roomDto;
    }

    private BasicMedicalProfileResponse getBasicMedicalProfile(Long customerId) {
        List<MedicalProfile> profiles = medicalProfileRepository
                .findByCustomerIdAndIsActiveTrue(customerId);

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
