package com.S_Health.GenderHealthCare.modules.health.dto.response;

import java.time.Instant;

/**
 * Safe response for deployment and load-balancer health checks.
 */
public record HealthResponse(
        String status,
        String application,
        Instant timestamp) {
}
