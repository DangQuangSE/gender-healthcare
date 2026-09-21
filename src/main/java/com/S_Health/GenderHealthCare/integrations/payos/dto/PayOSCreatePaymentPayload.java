package com.S_Health.GenderHealthCare.integrations.payos.dto;

public record PayOSCreatePaymentPayload(
        Long orderCode,
        Long amount,
        String description,
        String cancelUrl,
        String returnUrl,
        String signature
) {
}
