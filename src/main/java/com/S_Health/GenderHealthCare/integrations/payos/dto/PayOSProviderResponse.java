package com.S_Health.GenderHealthCare.integrations.payos.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PayOSProviderResponse(
        String code,
        String desc,
        PayOSProviderData data,
        String signature
) {
}
