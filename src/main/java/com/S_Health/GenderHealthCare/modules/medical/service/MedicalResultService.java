package com.S_Health.GenderHealthCare.modules.medical.service;

import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.medical.enums.ResultType;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.medical.domain.TreatmentProtocol;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalResult;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;


import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalResultResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.ConsultationResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.LabTestResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.ResultRequest;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalProfileRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.MedicalResultRepository;
import com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence.TreatmentProtocolRepository;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.AuthenticationRepository;
import com.S_Health.GenderHealthCare.modules.appointment.service.AppointmentStatusCalculator;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
public class MedicalResultService {
    private final MedicalResultRepository medicalResultRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthenticationRepository authenticationRepository;
    private final MedicalProfileRepository medicalProfileRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final AppointmentStatusCalculator statusCalculator;
    private final TreatmentProtocolRepository treatmentProtocolRepository;

    public MedicalResultService(
            MedicalResultRepository medicalResultRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            AppointmentRepository appointmentRepository,
            AuthenticationRepository authenticationRepository,
            MedicalProfileRepository medicalProfileRepository,
            ModelMapper modelMapper,
            AuthUtil authUtil,
            AppointmentStatusCalculator statusCalculator,
            TreatmentProtocolRepository treatmentProtocolRepository) {
        this.medicalResultRepository = medicalResultRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.appointmentRepository = appointmentRepository;
        this.authenticationRepository = authenticationRepository;
        this.medicalProfileRepository = medicalProfileRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
        this.statusCalculator = statusCalculator;
        this.treatmentProtocolRepository = treatmentProtocolRepository;
    }

    // === API MỚI - RIÊNG BIỆT CHO TỪNG LOẠI ===

    /**
     * Lưu kết quả tư vấn khám bệnh
     */
    @Transactional
    public MedicalResultResponse saveConsultationResult(ConsultationResultRequest request) {
        User writer = authenticationRepository.findById(authUtil.getCurrentUserId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.WRITER_NOT_FOUND));
        AppointmentDetail appointmentDetail = appointmentDetailRepository.findById(request.getAppointmentDetailId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.APPOINTMENT_DETAIL_NOT_FOUND));
        ensureConsultationWriter(writer, appointmentDetail);
        TreatmentProtocol protocol = treatmentProtocolRepository.findById(request.getTreatmentProtocolId())
                .orElseThrow(()-> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.TREATMENT_PROTOCOL_NOT_FOUND));


        MedicalResult medicalResult = MedicalResult.builder()
                .appointmentDetail(appointmentDetail)
                .consultant(writer)
                .resultType(ResultType.CONSULTATION)  // Cố định là CONSULTATION
                .description(request.getDescription())
                .diagnosis(request.getDiagnosis())
                .treatmentPlan(request.getTreatmentPlan())
                .treatmentProtocol(protocol)
                // Không set các field xét nghiệm (để null)
                .isActive(true)
                .build();

        // Lưu kết quả khám
        medicalResultRepository.save(medicalResult);

        // Cập nhật trạng thái appointment
        updateAppointmentStatus(appointmentDetail);

        return mapToFullMedicalResultResponse(medicalResult);
    }

    /**
     * Lưu kết quả xét nghiệm
     */
    @Transactional
    public MedicalResultResponse saveLabTestResult(LabTestResultRequest request) {
        User writer = authenticationRepository.findById(authUtil.getCurrentUserId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.WRITER_NOT_FOUND));
        AppointmentDetail appointmentDetail = appointmentDetailRepository.findById(request.getAppointmentDetailId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.APPOINTMENT_DETAIL_NOT_FOUND));
        ensureLabWriter(writer);
        TreatmentProtocol protocol = treatmentProtocolRepository.findById(request.getTreatmentProtocolId())
                .orElseThrow(()-> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.TREATMENT_PROTOCOL_NOT_FOUND));

        MedicalResult medicalResult = MedicalResult.builder()
                .appointmentDetail(appointmentDetail)
                .consultant(writer)
                .resultType(ResultType.LAB_TEST)  // Cố định là LAB_TEST
                .description(request.getDescription())
                .diagnosis(request.getDiagnosis())
                .treatmentPlan(request.getTreatmentPlan())
                // Thông tin xét nghiệm
                .testName(request.getTestName())
                .testResult(request.getTestResult())
                .normalRange(request.getNormalRange())
                .testMethod(request.getTestMethod())
                .specimenType(request.getSpecimenType())
                .testStatus(request.getTestStatus())
                .sampleCollectedAt(request.getSampleCollectedAt())
                .labNotes(request.getLabNotes())
                .treatmentProtocol(protocol)
                .isActive(true)
                .build();

        // Lưu kết quả xét nghiệm
        medicalResultRepository.save(medicalResult);

        // Tự động cập nhật thông tin y tế từ kết quả xét nghiệm
        updateMedicalProfileFromTestResult(medicalResult, appointmentDetail);

        // Cập nhật trạng thái appointment
        updateAppointmentStatus(appointmentDetail);

        return mapToFullMedicalResultResponse(medicalResult);
    }

    // === API CŨ - GIỮ LẠI ĐỂ BACKWARD COMPATIBILITY ===

    /**
     * Helper method để map MedicalResult sang MedicalResultResponse với đầy đủ thông tin
     */
    private MedicalResultResponse mapToFullMedicalResultResponse(MedicalResult result) {
        MedicalResultResponse dto = modelMapper.map(result, MedicalResultResponse.class);

        // Thêm thông tin liên quan
        AppointmentDetail appointmentDetail = result.getAppointmentDetail();
        if (appointmentDetail != null) {
            dto.setAppointmentDetailId(appointmentDetail.getId());
            dto.setServiceName(appointmentDetail.getService().getName());

            if (appointmentDetail.getAppointment() != null &&
                    appointmentDetail.getAppointment().getCustomer() != null) {
                dto.setPatientName(appointmentDetail.getAppointment().getCustomer().getFullname());
            }
        }

        if (result.getConsultant() != null) {
            dto.setConsultantName(result.getConsultant().getFullname());
        }

        return dto;
    }

    public MedicalResultResponse getResultById(Long id) {
        MedicalResult result = medicalResultRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.RESULT_NOT_FOUND_OR_DELETED));
        ensureCanView(result);

        return mapToFullMedicalResultResponse(result);
    }

    public List<MedicalResultResponse> getAllResultsByAppointmentDetail(Long appointmentDetailId) {
        AppointmentDetail appointmentDetail = appointmentDetailRepository.findById(appointmentDetailId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.APPOINTMENT_DETAIL_NOT_FOUND));
        ensureCanView(appointmentDetail);

        List<MedicalResult> results = medicalResultRepository
                .findAllByAppointmentDetailIdAndIsActiveTrue(appointmentDetailId);

        return results.stream()
                .map(this::mapToFullMedicalResultResponse)
                .toList();
    }

    @Transactional
    public MedicalResultResponse updateResult(Long id, ResultRequest request) {
        User writer = authenticationRepository.findById(authUtil.getCurrentUserId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.WRITER_NOT_FOUND));
        MedicalResult result = medicalResultRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.RESULT_UPDATE_NOT_FOUND));
        ensureCanModify(result, writer);
        result.setConsultant(writer);
        result.setDescription(request.getDescription());
        result.setDiagnosis(request.getDiagnosis());
        result.setTreatmentPlan(request.getTreatmentPlan());

        medicalResultRepository.save(result);
        return modelMapper.map(result, MedicalResultResponse.class);
    }

    @Transactional
    public void deleteResult(Long id) {
        MedicalResult result = medicalResultRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, MedicalMessages.RESULT_DELETE_NOT_FOUND));
        User currentUser = authUtil.getCurrentUser();
        ensureCanModify(result, currentUser);

        result.setIsActive(false);
        medicalResultRepository.save(result);
    }

    private void ensureConsultationWriter(User writer, AppointmentDetail appointmentDetail) {
        if (writer.getRole() != UserRole.CONSULTANT
                || appointmentDetail.getConsultant() == null
                || !Objects.equals(appointmentDetail.getConsultant().getId(), writer.getId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, MedicalMessages.RESULT_WRITE_FORBIDDEN);
        }
    }

    private void ensureLabWriter(User writer) {
        if (writer.getRole() != UserRole.STAFF
                && writer.getRole() != UserRole.ADMIN
                && writer.getRole() != UserRole.SUPER_ADMIN) {
            throw new DomainException(ErrorCode.FORBIDDEN, MedicalMessages.RESULT_WRITE_FORBIDDEN);
        }
    }

    private void ensureCanView(MedicalResult result) {
        ensureCanView(result.getAppointmentDetail());
    }

    private void ensureCanView(AppointmentDetail appointmentDetail) {
        User currentUser = authUtil.getCurrentUser();
        if (appointmentDetail == null) {
            throw new DomainException(ErrorCode.FORBIDDEN, MedicalMessages.RESULT_ACCESS_FORBIDDEN);
        }

        if (currentUser.getRole() == UserRole.ADMIN
                || currentUser.getRole() == UserRole.SUPER_ADMIN
                || currentUser.getRole() == UserRole.STAFF) {
            return;
        }

        Long currentUserId = currentUser.getId();
        boolean isCustomer = appointmentDetail.getAppointment() != null
                && appointmentDetail.getAppointment().getCustomer() != null
                && Objects.equals(appointmentDetail.getAppointment().getCustomer().getId(), currentUserId);
        boolean isAssignedConsultant = appointmentDetail.getConsultant() != null
                && Objects.equals(appointmentDetail.getConsultant().getId(), currentUserId);

        if (!isCustomer && !isAssignedConsultant) {
            throw new DomainException(ErrorCode.FORBIDDEN, MedicalMessages.RESULT_ACCESS_FORBIDDEN);
        }
    }

    private void ensureCanModify(MedicalResult result, User currentUser) {
        if (currentUser.getRole() == UserRole.ADMIN
                || currentUser.getRole() == UserRole.SUPER_ADMIN
                || currentUser.getRole() == UserRole.STAFF) {
            return;
        }

        if (currentUser.getRole() != UserRole.CONSULTANT
                || result.getConsultant() == null
                || !Objects.equals(result.getConsultant().getId(), currentUser.getId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, MedicalMessages.RESULT_WRITE_FORBIDDEN);
        }
    }

    private void updateMedicalProfileFromTestResult(MedicalResult result, AppointmentDetail appointmentDetail) {
        try {
            if (result.getResultType() != ResultType.LAB_TEST) {
                return;
            }
            // Tìm MedicalProfile của bệnh nhân
            User customer = appointmentDetail.getAppointment().getCustomer();
            com.S_Health.GenderHealthCare.modules.catalog.domain.Service service = appointmentDetail.getService();

            Optional<MedicalProfile> profileOpt = medicalProfileRepository
                    .findByCustomerAndServiceAndIsActiveTrue(customer, service);

            if (profileOpt.isEmpty()) {
                return; // Không có profile thì không cập nhật
            }
            MedicalProfile profile = profileOpt.get();

            //profile.setLastUpdatedBy(result.getConsultant().getId());
            medicalProfileRepository.save(profile);
        } catch (Exception e) {
            // Log error nhưng không throw để không ảnh hưởng đến việc lưu kết quả
            throw new DomainException(ErrorCode.INTERNAL_ERROR, MedicalMessages.PROFILE_UPDATE_FAILED, e);
        }
    }
    private void updateAppointmentStatus(AppointmentDetail appointmentDetail) {
        // Tự động cập nhật trạng thái AppointmentDetail thành COMPLETED
        appointmentDetail.setStatus(AppointmentStatus.COMPLETED);
        appointmentDetail.setUpdate_at(LocalDateTime.now());
        appointmentDetailRepository.save(appointmentDetail);

        // Tính toán lại trạng thái Appointment dựa trên tất cả AppointmentDetail
        Appointment appointment = appointmentDetail.getAppointment();
        if (appointment != null) {
            List<AppointmentDetail> allDetails = appointmentDetailRepository
                    .findByAppointmentAndIsActiveTrue(appointment);

            // Tính toán status mới theo quy tắc
            AppointmentStatus newStatus = statusCalculator.calculateStatus(allDetails);
            AppointmentStatus oldStatus = appointment.getStatus();

            if (oldStatus != newStatus) {
                appointment.setStatus(newStatus);
                appointment.setUpdate_at(LocalDateTime.now());
                appointmentRepository.save(appointment);
            }
        }
    }
}
