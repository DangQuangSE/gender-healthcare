package com.S_Health.GenderHealthCare.integrations.payos.dto;

public record PayOSPaymentLinkResponse(
        Long orderCode,
        Long amount,
        String paymentLinkId,
        String checkoutUrl,
        String status
) {
}
