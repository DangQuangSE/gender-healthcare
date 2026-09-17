package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;

import com.S_Health.GenderHealthCare.dto.ServiceDTO;
import com.S_Health.GenderHealthCare.dto.response.ComboResponse;
import com.S_Health.GenderHealthCare.modules.catalog.service.ServiceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.catalog.controller.ServiceCatalogController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyServiceController {
    @Autowired
    ServiceManagementService serviceManagementService;

    @GetMapping
    @Operation(summary = CatalogConstants.GET_SERVICES)
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceManagementService.getAllServices());
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_SERVICE)
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagementService.getServiceById(id));
    }

    @GetMapping("/search")
    @Operation(summary = CatalogConstants.SEARCH_SERVICES_BY_NAME)
    public ResponseEntity<List<ServiceDTO>> searchServicesByName(@RequestParam String name) {
        return ResponseEntity.ok(serviceManagementService.searchServicesByName(name));
    }

    @GetMapping("/specialization/{specializationId}")
    @Operation(summary = CatalogConstants.GET_SERVICES_BY_SPECIALIZATION)
    public ResponseEntity<List<ServiceDTO>> getServicesBySpecialization(@PathVariable Long specializationId) {
        return ResponseEntity.ok(serviceManagementService.getServicesBySpecialization(specializationId));
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_SERVICE)
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceManagementService.createService(serviceDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_SERVICE)
    public ResponseEntity<ServiceDTO> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceManagementService.updateService(id, serviceDTO));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = CatalogConstants.ACTIVATE_SERVICE)
    public ResponseEntity<ServiceDTO> activateService(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagementService.activateService(id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = CatalogConstants.DEACTIVATE_SERVICE)
    public ResponseEntity<ServiceDTO> deactivateService(@PathVariable Long id) {
        return ResponseEntity.ok(serviceManagementService.deactivateService(id));
    }

    @PostMapping("/combo")
    @Operation(summary = CatalogConstants.CREATE_COMBO_SERVICE)
    public ResponseEntity<ComboResponse> createComboService(@Valid @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceManagementService.createComboService(serviceDTO));
    }
}
