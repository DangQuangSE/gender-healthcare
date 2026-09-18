package com.S_Health.GenderHealthCare.modules.health.controller;

import com.S_Health.GenderHealthCare.modules.health.HealthMessages;
import com.S_Health.GenderHealthCare.modules.health.dto.response.HealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Liveness endpoint used by deployment and infrastructure checks.
 *
 * <p>This endpoint intentionally does not expose database or provider details.
 * It only confirms that the Spring application is serving requests.</p>
 */
@RestController
@RequestMapping(HealthMessages.HEALTH_PATH)
public class HealthController {
    private final String applicationName;

    public HealthController(
            @Value("${spring.application.name:GenderHealthCare}") String applicationName) {
        this.applicationName = applicationName;
    }

    @GetMapping
    public HealthResponse getHealth() {
        return new HealthResponse(
                HealthMessages.STATUS_UP,
                applicationName,
                Instant.now());
    }
}
