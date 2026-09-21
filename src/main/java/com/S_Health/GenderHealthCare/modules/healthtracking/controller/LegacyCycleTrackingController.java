package com.S_Health.GenderHealthCare.modules.healthtracking.controller;

import com.S_Health.GenderHealthCare.modules.healthtracking.dto.request.CycleTrackingRequest;
import com.S_Health.GenderHealthCare.modules.healthtracking.dto.response.CycleTrackingResponse;
import com.S_Health.GenderHealthCare.modules.healthtracking.service.CycleTrackingService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycle-track")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyCycleTrackingController {
    private final CycleTrackingService cycleTrackingService;
    private final AuthUtil authUtil;

    public LegacyCycleTrackingController(
            CycleTrackingService cycleTrackingService,
            AuthUtil authUtil) {
        this.cycleTrackingService = cycleTrackingService;
        this.authUtil = authUtil;
    }

    @PostMapping("/log")
    public ResponseEntity<CycleTrackingResponse> saveDailyLog(@RequestBody CycleTrackingRequest request) {
        CycleTrackingResponse response = cycleTrackingService.saveDailyLog(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<List<CycleTrackingRequest>> getLogs() {
        Long userId = authUtil.getCurrentUserId();
        return ResponseEntity.ok(cycleTrackingService.getLogsByUser(userId));
    }
}
