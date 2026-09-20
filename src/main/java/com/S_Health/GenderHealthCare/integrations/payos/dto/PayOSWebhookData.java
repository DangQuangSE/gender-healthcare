package com.S_Health.GenderHealthCare.integrations.payos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayOSWebhookData(
        Long orderCode,
        Long amount,
        String description,
        String accountNumber,
        String reference,
        String transactionDateTime,
        String currency,
        String paymentLinkId,
        String code,
        String desc,
        String counterAccountBankId,
        String counterAccountBankName,
        String counterAccountName,
        String counterAccountNumber,
        String virtualAccountName,
        String virtualAccountNumber
) {
}
