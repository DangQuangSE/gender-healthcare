package com.S_Health.GenderHealthCare.integrations.payos;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PayOSGatewayContractTest {

    @Test
    void providerBoundaryExposesCreateLookupAndWebhookVerificationOperations() throws Exception {
        Class<?> gateway = Class.forName(
                "com.S_Health.GenderHealthCare.integrations.payos.PayOSGateway");

        String[] methodNames = Arrays.stream(gateway.getDeclaredMethods())
                .map(Method::getName)
                .sorted()
                .toArray(String[]::new);

        assertThat(methodNames).contains(
                "createPaymentLink", "findPaymentStatus", "getPaymentStatus", "verifyWebhook");
    }
}
