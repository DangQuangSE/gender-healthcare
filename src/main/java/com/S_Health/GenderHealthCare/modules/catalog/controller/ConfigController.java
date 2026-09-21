package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ConfigCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ConfigUpdateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ConfigValueResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/config")
@Validated
public class ConfigController {
    private final CatalogService catalogService;

    public ConfigController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_CONFIGS)
    public ApiResponse<List<ConfigValueResponse>> getConfigs() {
        return ApiResponse.success(catalogService.getConfigs(), null);
    }

    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_CONFIG)
    public ApiResponse<ConfigValueResponse> createConfig(
            @Valid @ModelAttribute ConfigCreateRequest request) {
        return ApiResponse.success(
                catalogService.createConfig(request.getName(), request.getValue()),
                null);
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_CONFIG)
    public ApiResponse<ConfigValueResponse> updateConfig(
            @PathVariable Long id,
            @Valid @ModelAttribute ConfigUpdateRequest request) {
        return ApiResponse.success(catalogService.updateConfig(id, request.getValue()), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_CONFIG)
    public ApiResponse<String> deleteConfig(@PathVariable Long id) {
        catalogService.deleteConfig(id);
        return ApiResponse.success(CatalogConstants.CONFIGURATION_DELETED, null);
    }
}
