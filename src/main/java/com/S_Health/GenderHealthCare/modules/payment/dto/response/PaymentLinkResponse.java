package com.S_Health.GenderHealthCare.modules.payment.dto.response;

import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;

public record PaymentLinkResponse(
        Long orderCode,
        Long serviceAmount,
        Long chargedAmount,
        String checkoutUrl,
        String paymentLinkId,
        String status,
        PaymentIntent paymentIntent
) {
}
