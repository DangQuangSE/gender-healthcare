package com.S_Health.GenderHealthCare.modules.payment.dto.response;

import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;

public record PaymentStatusResponse(
        Long orderCode,
        Long serviceAmount,
        Long chargedAmount,
        String checkoutUrl,
        String paymentLinkId,
        PaymentStatus paymentStatus,
        PaymentIntent paymentIntent,
        String providerStatus,
        String message
) {
}
