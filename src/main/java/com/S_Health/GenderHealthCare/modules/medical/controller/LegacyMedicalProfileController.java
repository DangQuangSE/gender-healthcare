package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;

import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDTO;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.PatientMedicalHistoryDTO;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MedicalInfoUpdateRequest;
import com.S_Health.GenderHealthCare.modules.medical.service.MedicalProfileService;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MedicalInfoQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MyMedicalProfileQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.PatientHistoryQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medical-profile")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyMedicalProfileController {
    private final MedicalProfileService medicalProfileService;

    public LegacyMedicalProfileController(MedicalProfileService medicalProfileService) {
        this.medicalProfileService = medicalProfileService;
    }

    // API cho user xem profile của mình
    @GetMapping("/my-profile")
    @Operation(summary = MedicalMessages.GET_MY_PROFILE)
    public ResponseEntity getMyProfile(@Valid @ModelAttribute MyMedicalProfileQuery request) {
        return ResponseEntity.ok(medicalProfileService.getMyProfile(request));
    }

    // API cho bác sĩ xem lịch sử bệnh nhân (simplified)
    @GetMapping("/patient/{patientId}/history")
    @Operation(summary = MedicalMessages.GET_PATIENT_HISTORY)
    public ResponseEntity<PatientMedicalHistoryDTO> getPatientHistory(
            @PathVariable Long patientId,
            @Valid @ModelAttribute PatientHistoryQuery request) {

        PatientMedicalHistoryDTO history = medicalProfileService
                .getPatientHistory(patientId, request);
        return ResponseEntity.ok(history);
    }

    // API cho staff cập nhật thông tin y tế khi check-in
    @PutMapping("/update-medical-info")
    @Operation(summary = MedicalMessages.UPDATE_MEDICAL_INFO)
    public ResponseEntity<String> updateMedicalInfo(@RequestBody MedicalInfoUpdateRequest request) {
        medicalProfileService.updateMedicalInfo(request);
        return ResponseEntity.ok(MedicalMessages.MEDICAL_INFO_UPDATED_SUCCESS);
    }

    // API cho bác sĩ xem thông tin y tế chi tiết
    @GetMapping("/medical-info")
    @Operation(summary = MedicalMessages.GET_MEDICAL_INFO)
    public ResponseEntity<MedicalProfile> getMedicalInfo(
            @Valid @ModelAttribute MedicalInfoQuery request) {
        MedicalProfile profile = medicalProfileService.getMedicalInfoForDoctor(request);
        return ResponseEntity.ok(profile);
    }
}
