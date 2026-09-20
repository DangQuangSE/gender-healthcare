package com.S_Health.GenderHealthCare.integrations.payos.dto;

public record PayOSPaymentLinkRequest(
        Long orderCode,
        Long amount,
        String description,
        String cancelUrl,
        String returnUrl
) {
}
