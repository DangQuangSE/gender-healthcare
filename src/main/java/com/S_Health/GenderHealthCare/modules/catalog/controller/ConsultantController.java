package com.S_Health.GenderHealthCare.modules.catalog.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.catalog.dto.request.ConsultantQuery;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultants")
public class ConsultantController {
    private final CatalogService catalogService;

    public ConsultantController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = CatalogConstants.GET_CONSULTANTS)
    public ApiResponse<List<ConsultantResponse>> getConsultants(
            @Valid @ModelAttribute ConsultantQuery request) {
        return ApiResponse.success(catalogService.getConsultants(request.getServiceId()), null);
    }
}
