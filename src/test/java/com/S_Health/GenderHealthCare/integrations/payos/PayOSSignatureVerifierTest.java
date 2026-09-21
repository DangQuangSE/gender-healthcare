package com.S_Health.GenderHealthCare.integrations.payos;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PayOSSignatureVerifierTest {

    private static final String VERIFIER_CLASS =
            "com.S_Health.GenderHealthCare.integrations.payos.PayOSSignatureVerifier";
    private static final String CHECKSUM_KEY = "test-checksum-key";

    @Test
    void createsThePayOSPaymentRequestSignatureFromSortedFields() throws Exception {
        Object verifier = verifier();
        Method method = verifier.getClass().getMethod(
                "createPaymentSignature",
                long.class,
                String.class,
                String.class,
                long.class,
                String.class);

        String signature = (String) method.invoke(
                verifier,
                2000L,
                "https://example.com/cancel",
                "Test payment",
                123L,
                "https://example.com/return");

        assertThat(signature)
                .isEqualTo("6fbc0cc92c0fcaef754ced637d6b877931362fb8968592223d55f64b47b8af72");
    }

    @Test
    void verifiesWebhookDataAndRejectsTampering() throws Exception {
        Object verifier = verifier();
        Method method = verifier.getClass().getMethod("verifyWebhookSignature", Map.class, String.class);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("orderCode", 123L);
        data.put("amount", 3000L);
        data.put("description", "VQRIO123");
        String validSignature = "196be820e9d541137626087c3dbe3bc0a0dc373282278e6d46c874e016135614";

        assertThat(method.invoke(verifier, data, validSignature)).isEqualTo(true);

        data.put("amount", 3001L);
        assertThat(method.invoke(verifier, data, validSignature)).isEqualTo(false);
    }

    private Object verifier() throws Exception {
        Class<?> type = Class.forName(VERIFIER_CLASS);
        Constructor<?> constructor = type.getConstructor(String.class);
        return constructor.newInstance(CHECKSUM_KEY);
    }
}
