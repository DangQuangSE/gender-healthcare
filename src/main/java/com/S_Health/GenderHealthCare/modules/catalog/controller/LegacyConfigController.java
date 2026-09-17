package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.domain.ConfigValue;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ConfigCreateRequest;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ConfigUpdateRequest;

import com.S_Health.GenderHealthCare.modules.catalog.service.ConfigValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/config")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.catalog.controller.ConfigController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyConfigController {

    @Autowired
    ConfigValueService configValueService;
    
    @GetMapping
    @Operation(summary = CatalogConstants.GET_CONFIGS)
    public ResponseEntity<List<ConfigValue>> getAllConfigs() {
        return ResponseEntity.ok(configValueService.getAllConfigs());
    }

    @PutMapping("/{id}")
    @Operation(summary = CatalogConstants.UPDATE_CONFIG)
    public ResponseEntity<ConfigValue> updateConfig(
            @PathVariable Long id,
            @Valid @ModelAttribute ConfigUpdateRequest request) {
        ConfigValue updatedConfig = configValueService.updateConfig(id, request.getValue());
        return ResponseEntity.ok(updatedConfig);
    }
    
    @PostMapping
    @Operation(summary = CatalogConstants.CREATE_CONFIG)
    public ResponseEntity<ConfigValue> createConfig(
            @Valid @ModelAttribute ConfigCreateRequest request) {
        ConfigValue savedConfig = configValueService.createConfig(request.getName(), request.getValue());
        return ResponseEntity.ok(savedConfig);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = CatalogConstants.DELETE_CONFIG)
    public ResponseEntity<String> deleteConfig(@PathVariable Long id) {
        configValueService.deleteConfig(id);
        return ResponseEntity.ok(CatalogConstants.CONFIGURATION_DELETED);
    }
}
