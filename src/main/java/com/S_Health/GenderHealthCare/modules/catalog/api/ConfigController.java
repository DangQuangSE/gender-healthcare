package com.S_Health.GenderHealthCare.modules.catalog.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ConfigValueResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Operation(summary = "Get public configuration values")
    public ApiResponse<List<ConfigValueResponse>> getConfigs() {
        return ApiResponse.success(catalogService.getConfigs(), null);
    }

    @PostMapping
    @Operation(summary = "Create configuration value")
    public ApiResponse<ConfigValueResponse> createConfig(
            @NotBlank @RequestParam String name,
            @NotNull @RequestParam Integer value) {
        return ApiResponse.success(catalogService.createConfig(name, value), null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update configuration value")
    public ApiResponse<ConfigValueResponse> updateConfig(
            @PathVariable Long id,
            @NotNull @RequestParam Integer value) {
        return ApiResponse.success(catalogService.updateConfig(id, value), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete configuration value")
    public ApiResponse<String> deleteConfig(@PathVariable Long id) {
        catalogService.deleteConfig(id);
        return ApiResponse.success(CatalogConstants.CONFIGURATION_DELETED, null);
    }
}
