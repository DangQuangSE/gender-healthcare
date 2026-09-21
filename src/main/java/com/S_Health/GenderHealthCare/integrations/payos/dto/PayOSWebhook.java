package com.S_Health.GenderHealthCare.integrations.payos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayOSWebhook(
        String code,
        String desc,
        boolean success,
        PayOSWebhookData data,
        String signature
) {
}
