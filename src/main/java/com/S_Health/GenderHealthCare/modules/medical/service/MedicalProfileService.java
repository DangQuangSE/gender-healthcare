package com.S_Health.GenderHealthCare.modules.medical.service;

import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MedicalInfoQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MyMedicalProfileQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.PatientHistoryQuery;
import com.S_Health.GenderHealthCare.modules.medical.enums.ResultType;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalResult;
import com.S_Health.GenderHealthCare.modules.catalog.enums.ServiceType;
import com.S_Health.GenderHealthCare.modules.medical.enums.TestStatus;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;


import com.S_Health.GenderHealthCare.dto.*;
import com.S_Health.GenderHealthCare.dto.request.MedicalInfoUpdateRequest;
import com.S_Health.GenderHealthCare.dto.response.MedicalProfileDTO;

import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.repository.*;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

@Service
public class MedicalProfileService {
    private final MedicalProfileRepository medicalProfileRepository;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final MedicalResultRepository medicalResultRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    public MedicalProfileService(
            MedicalProfileRepository medicalProfileRepository,
            ServiceRepository serviceRepository,
            AppointmentRepository appointmentRepository,
            AuthenticationRepository authenticationRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            MedicalResultRepository medicalResultRepository,
            AuthUtil authUtil,
            ModelMapper modelMapper) {
        this.medicalProfileRepository = medicalProfileRepository;
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.authenticationRepository = authenticationRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.medicalResultRepository = medicalResultRepository;
        this.authUtil = authUtil;
        this.modelMapper = modelMapper;
    }

    public void createMedicalProfile(Appointment appointment) {
        User user = authUtil.getCurrentUser();
        com.S_Health.GenderHealthCare.modules.catalog.domain.Service service = serviceRepository.findById(appointment.getService().getId())
                .orElseThrow(() -> new AppException(MedicalMessages.SERVICE_NOT_FOUND));
        // Tìm MedicalProfile đã tồn tại
        Optional<MedicalProfile> existingProfile = medicalProfileRepository
                .findByCustomerAndServiceAndIsActiveTrue(user, service);
        MedicalProfile medicalProfile;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);
        if (existingProfile.isPresent()) {
            medicalProfile = existingProfile.get();
        } else {
            // Tạo mới nếu chưa tồn tại
            medicalProfile = new MedicalProfile();
            medicalProfile.setCustomer(user);
            medicalProfile.setService(service);
            medicalProfile = medicalProfileRepository.save(medicalProfile);
        }
        // Gán medicalProfile cho appointment và lưu
        appointment.setMedicalProfile(medicalProfile);
        appointmentRepository.save(appointment);
        medicalProfile.setAppointments(appointments);
        medicalProfileRepository.save(medicalProfile);
    }

    public MedicalProfileDTO getMyProfile(MyMedicalProfileQuery request) {
        Long serviceId = request.getServiceId();
        User user = authUtil.getCurrentUser();
        com.S_Health.GenderHealthCare.modules.catalog.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(MedicalMessages.SERVICE_NOT_FOUND));
        MedicalProfile medicalProfile = medicalProfileRepository.findByCustomerAndServiceAndIsActiveTrue(user, service)
                .orElseThrow(() -> new AppException(MedicalMessages.MEDICAL_PROFILE_NOT_FOUND));
        return modelMapper.map(medicalProfile, MedicalProfileDTO.class);
    }
    /**
     * Xem lịch sử khám bệnh cần thiết của bệnh nhân (cho bác sĩ)
     */
    public PatientMedicalHistoryDTO getPatientHistory(
            Long patientId,
            PatientHistoryQuery request) {
        int page = request.getPage();
        int size = request.getSize();
        User currentDoctor = authUtil.getCurrentUser();

        // Kiểm tra quyền truy cập
        if (currentDoctor.getRole() != UserRole.CONSULTANT) {
            throw new AppException(MedicalMessages.HISTORY_ACCESS_FORBIDDEN);
        }

        // Lấy thông tin bệnh nhân
        User patient = authenticationRepository.findById(patientId)
                .orElseThrow(() -> new AppException(MedicalMessages.PATIENT_NOT_FOUND));

        // Kiểm tra bác sĩ có quyền xem bệnh nhân này không
        boolean hasAccess = appointmentDetailRepository
                .existsByConsultantIdAndAppointmentCustomerId(currentDoctor.getId(), patientId);
        if (!hasAccess) {
            throw new AppException(MedicalMessages.PROFILE_ACCESS_FORBIDDEN);
        }

        // Approach mới: Lấy 5 appointments gần nhất của bệnh nhân (đơn giản và hiệu quả)
        List<Appointment> recentAppointments = appointmentRepository
                .findAll().stream()
                .filter(appointment -> appointment.getCustomer().getId() == patientId)
                .filter(appointment -> appointment.getIsActive())
                .sorted((a1, a2) -> a2.getCreated_at().compareTo(a1.getCreated_at()))
                .limit(5) // Chỉ lấy 5 appointments gần nhất
                .collect(Collectors.toList());

        // Tạo Page từ list (không cần phân trang phức tạp vì chỉ có 5 records)
        Pageable pageable = PageRequest.of(0, 5);
        Page<Appointment> appointmentPage = PageableExecutionUtils.getPage(
                recentAppointments, pageable, () -> recentAppointments.size());

        // Lấy medical results từ các appointment details của 5 appointments này
        List<RecentTestResultDTO> recentTests = buildRecentTestsFromAppointments(recentAppointments);

        return PatientMedicalHistoryDTO.builder()
                .patientInfo(buildPatientInfo(patient))
                .appointments(buildAppointmentHistory(appointmentPage))
                .recentTests(recentTests)
                .totalVisits(recentAppointments.size())
                .build();
    }



    // Helper methods theo approach mới - đơn giản và hiệu quả
    private PatientBasicInfoDTO buildPatientInfo(User patient) {
        int age = patient.getDateOfBirth() != null ?
                Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears() : 0;

        // Lấy thông tin y tế từ MedicalProfile mới nhất (aggregate từ tất cả services)
        List<MedicalProfile> profiles = medicalProfileRepository
                .findByCustomerIdAndIsActiveTrue(patient.getId());

        MedicalProfile latestProfile = profiles.stream()
                .filter(p -> p.getAllergies() != null || p.getFamilyHistory() != null ||
                           p.getLifestyleNotes() != null || p.getSpecialNotes() != null)
                .max((p1, p2) -> p1.getUpdatedAt().compareTo(p2.getUpdatedAt()))
                .orElse(null);

        return PatientBasicInfoDTO.builder()
                .fullname(patient.getFullname())
                .age(age)
                .gender(patient.getGender() != null ? patient.getGender().toString() : null)
                .email(patient.getEmail())
                .phone(patient.getPhone())
                // Thông tin y tế quan trọng
                .allergies(latestProfile != null ? latestProfile.getAllergies() : null)
                .familyHistory(latestProfile != null ? latestProfile.getFamilyHistory() : null)
                .lifestyleNotes(latestProfile != null ? latestProfile.getLifestyleNotes() : null)
                .specialNotes(latestProfile != null ? latestProfile.getSpecialNotes() : null)
                .build();
    }



    private Page<AppointmentHistoryDTO> buildAppointmentHistory(Page<Appointment> appointmentPage) {
        return appointmentPage.map(appointment -> {
            // Lấy AppointmentDetail đầu tiên để lấy thông tin
            AppointmentDetail firstDetail = appointment.getAppointmentDetails().stream()
                    .filter(detail -> detail.getIsActive())
                    .findFirst()
                    .orElse(null);

            String doctorName = firstDetail != null ?
                    firstDetail.getConsultant().getFullname() : MedicalMessages.CONSULTANT_UNASSIGNED;

            String roomName = getRoomDisplayName(firstDetail);

            // Lấy diagnosis từ MedicalResult nếu có
            String diagnosis = appointment.getAppointmentDetails().stream()
                    .filter(detail -> detail.getIsActive())
                    .map(detail -> medicalResultRepository.findByAppointmentDetail(detail))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(MedicalResult::getDiagnosis)
                    .filter(d -> d != null && !d.trim().isEmpty())
                    .findFirst()
                    .orElse(MedicalMessages.DIAGNOSIS_UNAVAILABLE);

            return AppointmentHistoryDTO.builder()
                    .date(appointment.getPreferredDate())
                    .service(appointment.getService().getName())
                    .doctor(doctorName)
                    .room(roomName)
                    .status(appointment.getStatus().toString())
                    .diagnosis(diagnosis)
                    .build();
        });
    }

    /**
     * Approach mới: Lấy medical results từ 5 appointments gần nhất
     */
    private List<RecentTestResultDTO> buildRecentTestsFromAppointments(List<Appointment> recentAppointments) {
        try {
            List<RecentTestResultDTO> results = new ArrayList<>();

            for (Appointment appointment : recentAppointments) {
                // Lấy tất cả appointment details của appointment này
                List<AppointmentDetail> details = appointment.getAppointmentDetails().stream()
                        .filter(detail -> detail.getIsActive())
                        .collect(Collectors.toList());

                // Lấy medical results từ các appointment details
                for (AppointmentDetail detail : details) {
                    Optional<MedicalResult> resultOpt = medicalResultRepository.findByAppointmentDetail(detail);
                    if (resultOpt.isPresent()) {
                        MedicalResult result = resultOpt.get();

                        // Chỉ lấy LAB_TEST results
                        if (result.getResultType() == ResultType.LAB_TEST &&
                            result.getTestName() != null && !result.getTestName().trim().isEmpty()) {

                            results.add(RecentTestResultDTO.builder()
                                    .testName(result.getTestName())
                                    .result(result.getTestResult())
                                    .testDate(result.getCreatedAt().toLocalDate())
                                    .isAbnormal(result.getTestStatus() == TestStatus.ABNORMAL ||
                                               result.getTestStatus() == TestStatus.CRITICAL)
                                    .build());
                        }
                    }
                }
            }

            // Sort theo ngày mới nhất và limit 5
            return results.stream()
                    .sorted((r1, r2) -> r2.getTestDate().compareTo(r1.getTestDate()))
                    .limit(5)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Cập nhật thông tin y tế cơ bản khi check-in (cho staff)
     */
    public MedicalProfile updateMedicalInfo(MedicalInfoUpdateRequest request) {
        User currentStaff = authUtil.getCurrentUser();

        // Kiểm tra quyền (chỉ staff và admin)
        if (currentStaff.getRole() != UserRole.STAFF && currentStaff.getRole() != UserRole.ADMIN) {
            throw new AppException(MedicalMessages.MEDICAL_INFO_UPDATE_FORBIDDEN);
        }

        // Lấy customer và service
        User customer = authenticationRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new AppException(MedicalMessages.PATIENT_NOT_FOUND));

        com.S_Health.GenderHealthCare.modules.catalog.domain.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(MedicalMessages.SERVICE_NOT_FOUND));

        // Tìm hoặc tạo medical profile
        MedicalProfile profile = medicalProfileRepository
                .findByCustomerAndServiceAndIsActiveTrue(customer, service)
                .orElse(MedicalProfile.builder()
                        .customer(customer)
                        .service(service)
                        .isActive(true)
                        .build());

        // Cập nhật thông tin cơ bản mà staff được phép nhập
        profile.setAllergies(request.getAllergies());
        profile.setChronicConditions(request.getChronicConditions());
        profile.setFamilyHistory(request.getFamilyHistory());
        profile.setLifestyleNotes(request.getLifestyleNotes());
        profile.setSpecialNotes(request.getSpecialNotes());
        profile.setEmergencyContact(request.getEmergencyContact());
        profile.setLastUpdatedBy(currentStaff.getId());

        return medicalProfileRepository.save(profile);
    }

    /**
     * Lấy thông tin y tế để hiển thị cho bác sĩ
     */
    public MedicalProfile getMedicalInfoForDoctor(MedicalInfoQuery request) {
        Long customerId = request.getCustomerId();
        Long serviceId = request.getServiceId();
        User customer = authenticationRepository.findById(customerId)
                .orElseThrow(() -> new AppException(MedicalMessages.PATIENT_NOT_FOUND));

        com.S_Health.GenderHealthCare.modules.catalog.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new AppException(MedicalMessages.SERVICE_NOT_FOUND));

        return medicalProfileRepository
                .findByCustomerAndServiceAndIsActiveTrue(customer, service)
                .orElse(null); // Trả về null nếu chưa có thông tin
    }

    /**
     * Lấy tên phòng để hiển thị, xử lý trường hợp consulting online
     */
    private String getRoomDisplayName(AppointmentDetail appointmentDetail) {
        if (appointmentDetail == null) {
            return MedicalMessages.CONSULTANT_UNASSIGNED;
        }

        // Kiểm tra nếu là consulting online
        if (appointmentDetail.getService() != null &&
            appointmentDetail.getService().getType() == ServiceType.CONSULTING_ON) {
            return MedicalMessages.ONLINE_CONSULTATION;
        }

        // Trường hợp khác, hiển thị tên phòng
        if (appointmentDetail.getRoom() != null) {
            return appointmentDetail.getRoom().getName();
        }

        return MedicalMessages.ROOM_UNASSIGNED;
    }
}

