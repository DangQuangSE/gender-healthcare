package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationDetailResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.SpecializationRequest;
import com.S_Health.GenderHealthCare.modules.catalog.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.catalog.controller.SpecializationController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacySpecializationController {
    private final SpecializationService specializationService;

    public LegacySpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_SPECIALIZATIONS)
    public ResponseEntity<List<SpecializationDetailResponse>> getAllSpecializations() {
        return ResponseEntity.ok(specializationService.getAllSpecializations());
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_SPECIALIZATION)
    public ResponseEntity<SpecializationDetailResponse> getSpecializationById(@PathVariable Long id) {
        return ResponseEntity.ok(specializationService.getSpecializationById(id));
    }

    @GetMapping("/search")
    @Operation(summary = CatalogConstants.GET_SPECIALIZATIONS)
    public ResponseEntity<List<SpecializationDetailResponse>> searchSpecializationsByName(@RequestParam String name) {
        return ResponseEntity.ok(specializationService.searchSpecializationsByName(name));
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_SPECIALIZATION)
    public ResponseEntity<SpecializationDetailResponse> createSpecialization(@Valid @RequestBody SpecializationRequest request) {
        return ResponseEntity.ok(specializationService.createSpecialization(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_SPECIALIZATION)
    public ResponseEntity<SpecializationDetailResponse> updateSpecialization(
            @PathVariable Long id,
            @Valid @RequestBody SpecializationRequest request) {
        return ResponseEntity.ok(specializationService.updateSpecialization(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_SPECIALIZATION)
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecialization(id);
        return ResponseEntity.noContent().build();
    }
}
