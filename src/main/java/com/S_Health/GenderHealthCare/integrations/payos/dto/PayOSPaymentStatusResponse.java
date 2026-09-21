package com.S_Health.GenderHealthCare.integrations.payos.dto;

public record PayOSPaymentStatusResponse(
        Long orderCode,
        Long amount,
        Long amountPaid,
        Long amountRemaining,
        String paymentLinkId,
        String checkoutUrl,
        String status
) {
}
