package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationQuery;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specializations")
public class SpecializationController {
    private final CatalogService catalogService;

    public SpecializationController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_SPECIALIZATIONS)
    public ApiResponse<List<SpecializationResponse>> getSpecializations(
            @Valid @ModelAttribute SpecializationQuery request) {
        return ApiResponse.success(catalogService.getSpecializations(request.getName()), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_SPECIALIZATION)
    public ApiResponse<SpecializationResponse> getSpecialization(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getSpecialization(id), null);
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_SPECIALIZATION)
    public ApiResponse<SpecializationResponse> createSpecialization(
            @Valid @RequestBody SpecializationRequest request) {
        return ApiResponse.success(catalogService.createSpecialization(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_SPECIALIZATION)
    public ApiResponse<SpecializationResponse> updateSpecialization(
            @PathVariable Long id,
            @Valid @RequestBody SpecializationRequest request) {
        return ApiResponse.success(catalogService.updateSpecialization(id, request), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_SPECIALIZATION)
    public ApiResponse<String> deleteSpecialization(@PathVariable Long id) {
        catalogService.deleteSpecialization(id);
        return ApiResponse.success(CatalogConstants.SPECIALIZATION_DELETED, null);
    }
}
