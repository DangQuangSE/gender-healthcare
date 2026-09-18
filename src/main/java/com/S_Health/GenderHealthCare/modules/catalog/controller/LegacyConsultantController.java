package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;


import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.consultant.ConsultantDetailResponse;
import com.S_Health.GenderHealthCare.modules.user.service.ManageUserService;
import com.S_Health.GenderHealthCare.modules.scheduling.service.ServiceSlotPoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants")
@SecurityRequirement(name = "api")
@Tag(name = CatalogConstants.CONSULTANT_TAG, description = CatalogConstants.CONSULTANT_TAG_DESCRIPTION)
public class LegacyConsultantController {
    private final ManageUserService manageUserService;

    public LegacyConsultantController(ManageUserService manageUserService) {
        this.manageUserService = manageUserService;
    }

    @GetMapping("/by-service/{serviceId}")
    @Operation(summary = CatalogConstants.GET_CONSULTANTS)
    public ResponseEntity<List<ConsultantDetailResponse>> getConsultantsByService(@PathVariable Long serviceId) {
        List<ConsultantDetailResponse> consultants = manageUserService.getConsultantsByService(serviceId);
        return ResponseEntity.ok(consultants);
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_ALL_CONSULTANTS)
    public ResponseEntity<List<ConsultantDetailResponse>> getAllConsultants() {
        List<ConsultantDetailResponse> consultants = manageUserService.getUsersByRole("CONSULTANT");
        return ResponseEntity.ok(consultants);
    }
}
