package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.PatientMedicalHistoryResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MedicalInfoUpdateRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalInfoResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MedicalInfoQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.MyMedicalProfileQuery;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.PatientHistoryQuery;
import com.S_Health.GenderHealthCare.modules.medical.mapper.MedicalMapper;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalProfileResponse;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.medical.service.MedicalProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medical-profiles")
public class MedicalProfileController {
    private final MedicalProfileService medicalProfileService;
    private final MedicalMapper medicalMapper;

    public MedicalProfileController(
            MedicalProfileService medicalProfileService,
            MedicalMapper medicalMapper) {
        this.medicalProfileService = medicalProfileService;
        this.medicalMapper = medicalMapper;
    }

    @GetMapping("/me")
    @Operation(summary = MedicalMessages.GET_MY_PROFILE)
    public ApiResponse<MedicalProfileResponse> getMyProfile(
            @Valid @ModelAttribute MyMedicalProfileQuery request) {
        return ApiResponse.success(medicalProfileService.getMyProfile(request), null);
    }

    @GetMapping("/patients/{patientId}/history")
    @Operation(summary = MedicalMessages.GET_PATIENT_HISTORY)
    public ApiResponse<PatientMedicalHistoryResponse> getPatientHistory(
            @PathVariable Long patientId,
            @Valid @ModelAttribute PatientHistoryQuery request) {
        return ApiResponse.success(
                medicalProfileService.getPatientHistory(patientId, request),
                null);
    }

    @PutMapping("/medical-info")
    @Operation(summary = MedicalMessages.UPDATE_MEDICAL_INFO)
    public ApiResponse<String> updateMedicalInfo(
            @Valid @RequestBody MedicalInfoUpdateRequest request) {
        medicalProfileService.updateMedicalInfo(request);
        return ApiResponse.success(MedicalMessages.MEDICAL_INFO_UPDATED, null);
    }

    @GetMapping("/medical-info")
    @Operation(summary = MedicalMessages.GET_MEDICAL_INFO)
    public ApiResponse<MedicalInfoResponse> getMedicalInfo(
            @Valid @ModelAttribute MedicalInfoQuery request) {
        return ApiResponse.success(
                        medicalMapper.toMedicalInfoResponse(
                        medicalProfileService.getMedicalInfoForDoctor(request)),
                null);
    }
}
