package com.S_Health.GenderHealthCare.modules.healthtracking.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.dto.request.service.CycleTrackingRequest;
import com.S_Health.GenderHealthCare.dto.response.CycleTrackingResponse;
import com.S_Health.GenderHealthCare.modules.healthtracking.dto.response.CycleLogResponse;
import com.S_Health.GenderHealthCare.modules.healthtracking.mapper.CycleTrackingMapper;
import com.S_Health.GenderHealthCare.service.CycleTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/health-tracking/cycles")
public class CycleTrackingController {
    private final CycleTrackingService cycleTrackingService;
    private final CurrentUserProvider currentUserProvider;
    private final CycleTrackingMapper cycleTrackingMapper;

    public CycleTrackingController(
            CycleTrackingService cycleTrackingService,
            CurrentUserProvider currentUserProvider,
            CycleTrackingMapper cycleTrackingMapper) {
        this.cycleTrackingService = cycleTrackingService;
        this.currentUserProvider = currentUserProvider;
        this.cycleTrackingMapper = cycleTrackingMapper;
    }

    @PostMapping("/logs")
    @Operation(summary = "Save a daily cycle log")
    public ApiResponse<CycleTrackingResponse> saveDailyLog(
            @Valid @RequestBody CycleTrackingRequest request) {
        return ApiResponse.success(cycleTrackingService.saveDailyLog(request), null);
    }

    @GetMapping("/logs")
    @Operation(summary = "Get my cycle logs")
    public ApiResponse<List<CycleLogResponse>> getLogs() {
        return ApiResponse.success(
                cycleTrackingService.getLogsByUser(currentUserProvider.requireUserId()).stream()
                        .map(cycleTrackingMapper::toResponse)
                        .toList(),
                null);
    }
}
