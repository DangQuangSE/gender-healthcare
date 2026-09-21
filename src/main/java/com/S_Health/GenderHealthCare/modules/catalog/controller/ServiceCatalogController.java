package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ServiceQuery;
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
    @Operation(summary = CatalogConstants.GET_SERVICES)
    public ApiResponse<List<ServiceResponse>> getServices(
            @ModelAttribute ServiceQuery request) {
        return ApiResponse.success(catalogService.getServices(request), null);
    }

    @GetMapping("/{id}")
    @Operation(summary = CatalogConstants.GET_SERVICE)
    public ApiResponse<ServiceResponse> getService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.getService(id), null);
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_SERVICE)
    public ApiResponse<ServiceResponse> createService(
            @Valid @RequestBody ServiceCreateRequest request) {
        return ApiResponse.success(catalogService.createService(request), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_SERVICE)
    public ApiResponse<ServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody ServiceUpdateRequest request) {
        return ApiResponse.success(catalogService.updateService(id, request), null);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = CatalogConstants.ACTIVATE_SERVICE)
    public ApiResponse<ServiceResponse> activateService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.activateService(id), null);
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = CatalogConstants.DEACTIVATE_SERVICE)
    public ApiResponse<ServiceResponse> deactivateService(@PathVariable Long id) {
        return ApiResponse.success(catalogService.deactivateService(id), null);
    }

    @PostMapping("/combo")
    @Operation(summary = CatalogConstants.CREATE_COMBO_SERVICE)
    public ApiResponse<ComboServiceResponse> createComboService(
            @Valid @RequestBody ServiceCreateRequest request) {
        return ApiResponse.success(catalogService.createComboService(request), null);
    }
}
