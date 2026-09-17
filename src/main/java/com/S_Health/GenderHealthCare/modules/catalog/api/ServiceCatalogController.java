package com.S_Health.GenderHealthCare.modules.catalog.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceUpdateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ComboServiceResponse;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceCatalogController {
    private final CatalogService catalogService;

    public ServiceCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = "Get active services with optional filters")
    public ApiResponse<List<ServiceResponse>> getServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long specializationId) {
        return ApiResponse.success(catalogService.getServices(name, specializationId), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ApiResponse<ServiceResponse> getService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getService(id), null);
    }

    @PostMapping
    @Operation(summary = "Create service")
    public ApiResponse<ServiceResponse> createService(
            @Valid @RequestBody ServiceCreateRequest request) {
        return ApiResponse.success(catalogService.createService(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update service")
    public ApiResponse<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceUpdateRequest request) {
        return ApiResponse.success(catalogService.updateService(id, request), null);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate service")
    public ApiResponse<ServiceResponse> activateService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.activateService(id), null);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate service")
    public ApiResponse<ServiceResponse> deactivateService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.deactivateService(id), null);
    }

    @PostMapping("/combo")
    @Operation(summary = "Create combo service")
    public ApiResponse<ComboServiceResponse> createComboService(
            @Valid @RequestBody ServiceCreateRequest request) {
        return ApiResponse.success(catalogService.createComboService(request), null);
    }
}
