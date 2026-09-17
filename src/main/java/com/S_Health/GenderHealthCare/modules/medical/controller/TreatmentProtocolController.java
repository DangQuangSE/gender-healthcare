package com.S_Health.GenderHealthCare.modules.medical.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.TreatmentProtocolRequest;
import com.S_Health.GenderHealthCare.dto.response.TreatmentProtocolResponse;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import com.S_Health.GenderHealthCare.modules.medical.service.TreatmentProtocolService;
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
@RequestMapping("/api/v1/treatment-protocols")
public class TreatmentProtocolController {
    private final TreatmentProtocolService treatmentProtocolService;

    public TreatmentProtocolController(TreatmentProtocolService treatmentProtocolService) {
        this.treatmentProtocolService = treatmentProtocolService;
    }

    @PostMapping
    @Operation(summary = MedicalMessages.CREATE_PROTOCOL)
    public ApiResponse<TreatmentProtocolResponse> create(
            @Valid @RequestBody TreatmentProtocolRequest request) {
        return ApiResponse.success(treatmentProtocolService.create(request), null);
    }

    @GetMapping
    public ApiResponse<List<TreatmentProtocolResponse>> getAll() {
        return ApiResponse.success(treatmentProtocolService.getAll(), null);
    }

    @GetMapping("/{id}")
    public ApiResponse<TreatmentProtocolResponse> getById(@PathVariable Long id) {
        return ApiResponse.success(treatmentProtocolService.getById(id), null);
    }

    @PutMapping("/{id}")
    public ApiResponse<TreatmentProtocolResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentProtocolRequest request) {
        return ApiResponse.success(treatmentProtocolService.update(id, request), null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        treatmentProtocolService.delete(id);
    }
}
