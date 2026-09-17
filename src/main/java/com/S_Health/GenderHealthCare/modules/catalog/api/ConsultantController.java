package com.S_Health.GenderHealthCare.modules.catalog.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.catalog.service.CatalogService;
import com.S_Health.GenderHealthCare.modules.user.dto.response.ConsultantResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Get consultants with optional service filter")
    public ApiResponse<List<ConsultantResponse>> getConsultants(
            @RequestParam(required = false) Long serviceId) {
        return ApiResponse.success(catalogService.getConsultants(serviceId), null);
    }
}
