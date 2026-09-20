package com.S_Health.GenderHealthCare.integrations.payos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayOSProviderData(
        Long orderCode,
        Long amount,
        Long amountPaid,
        Long amountRemaining,
        String paymentLinkId,
        String checkoutUrl,
        String status
) {
}
