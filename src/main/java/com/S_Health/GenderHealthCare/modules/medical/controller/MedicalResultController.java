package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.ResultDTO;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.ConsultationResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.LabTestResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.ResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.medical.service.MedicalResultService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-results")
public class MedicalResultController {
    private final MedicalResultService medicalResultService;

    public MedicalResultController(MedicalResultService medicalResultService) {
        this.medicalResultService = medicalResultService;
    }

    @PostMapping("/consultations")
    @Operation(summary = MedicalMessages.CREATE_CONSULTATION_RESULT)
    public ApiResponse<ResultDTO> createConsultationResult(
            @Valid @RequestBody ConsultationResultRequest request) {
        return ApiResponse.success(medicalResultService.saveConsultationResult(request), null);
    }

    @PostMapping("/lab-tests")
    @Operation(summary = MedicalMessages.CREATE_LAB_RESULT)
    public ApiResponse<ResultDTO> createLabTestResult(
            @Valid @RequestBody LabTestResultRequest request) {
        return ApiResponse.success(medicalResultService.saveLabTestResult(request), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ResultDTO> getResultById(@PathVariable Long id) {
        return ApiResponse.success(medicalResultService.getResultById(id), null);
    }

    @GetMapping("/appointment-details/{id}")
    public ApiResponse<List<ResultDTO>> getResultsByAppointmentDetail(@PathVariable Long id) {
        return ApiResponse.success(
                medicalResultService.getAllResultsByAppointmentDetail(id),
                null);
    }

    @PutMapping("/{id}")
    public ApiResponse<ResultDTO> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody ResultRequest request) {
        return ApiResponse.success(medicalResultService.updateResult(id, request), null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteResult(@PathVariable Long id) {
        medicalResultService.deleteResult(id);
        return ApiResponse.success(MedicalMessages.RESULT_DELETED, null);
    }
}
