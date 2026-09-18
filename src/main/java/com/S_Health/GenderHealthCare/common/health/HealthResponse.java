package com.S_Health.GenderHealthCare.common.health;

import java.time.Instant;

/**
 * Safe response for deployment and load-balancer health checks.
 */
public record HealthResponse(
        String status,
        String application,
        Instant timestamp) {
}
