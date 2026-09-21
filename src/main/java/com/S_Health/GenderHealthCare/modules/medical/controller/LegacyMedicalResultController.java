package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;


import com.S_Health.GenderHealthCare.modules.medical.dto.request.ConsultationResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.LabTestResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.dto.request.ResultRequest;
import com.S_Health.GenderHealthCare.modules.medical.service.MedicalResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/result")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
@Tag(name = MedicalMessages.MEDICAL_RESULT_TAG, description = MedicalMessages.MEDICAL_RESULT_TAG_DESCRIPTION)
public class LegacyMedicalResultController {
    private final MedicalResultService medicalResultService;

    public LegacyMedicalResultController(MedicalResultService medicalResultService) {
        this.medicalResultService = medicalResultService;
    }

    @PostMapping("/consultation")
    @Operation(summary = MedicalMessages.CREATE_CONSULTATION_RESULT)
    public ResponseEntity inputConsultationResult(@RequestBody @Valid ConsultationResultRequest request) {
        return ResponseEntity.ok(medicalResultService.saveConsultationResult(request));
    }

    @PostMapping("/lab-test")
    @Operation(summary = MedicalMessages.CREATE_LAB_RESULT)
    public ResponseEntity inputLabTestResult(@RequestBody @Valid LabTestResultRequest request) {
        return ResponseEntity.ok(medicalResultService.saveLabTestResult(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalResultService.getResultById(id));
    }

    @GetMapping("/appointment-detail/{id}")
    public ResponseEntity getResultsByAppointmentDetail(@PathVariable Long id) {
        return ResponseEntity.ok(medicalResultService.getAllResultsByAppointmentDetail(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateResult(
            @PathVariable Long id,
            @Valid @RequestBody ResultRequest request) {
        return ResponseEntity.ok(medicalResultService.updateResult(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteResult(@PathVariable Long id) {
        medicalResultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
