package com.S_Health.GenderHealthCare.integrations.payos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "payos")
public class PayOSConfig {

    @NotBlank
    private String clientId;

    @NotBlank
    private String apiKey;

    @NotBlank
    private String checksumKey;

    @NotBlank
    private String apiBaseUrl;

    @NotBlank
    private String returnUrl;

    @NotBlank
    private String cancelUrl;

    @NotBlank
    private String webhookUrl;

    private int connectTimeoutSeconds = 5;
    private int readTimeoutSeconds = 10;
}
