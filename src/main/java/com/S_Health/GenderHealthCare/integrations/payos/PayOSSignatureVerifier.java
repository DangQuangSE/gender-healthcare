package com.S_Health.GenderHealthCare.integrations.payos;

import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PayOSSignatureVerifier {

    private final String checksumKey;

    public PayOSSignatureVerifier(String checksumKey) {
        this.checksumKey = Objects.requireNonNull(checksumKey, "checksumKey must not be null");
    }

    public String createPaymentSignature(
            long amount,
            String cancelUrl,
            String description,
            long orderCode,
            String returnUrl) {
        Map<String, Object> fields = Map.of(
                "amount", amount,
                "cancelUrl", cancelUrl,
                "description", description,
                "orderCode", orderCode,
                "returnUrl", returnUrl);
        return sign(fields);
    }

    public boolean verifyWebhookSignature(Map<String, Object> data, String signature) {
        if (data == null || signature == null || signature.isBlank()) {
            return false;
        }

        byte[] expected = hexToBytes(sign(data));
        byte[] actual = hexToBytes(signature);
        return expected.length == actual.length && MessageDigest.isEqual(expected, actual);
    }

    public String sign(Map<String, Object> fields) {
        String canonicalData = new TreeMap<>(fields).entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Objects.toString(entry.getValue(), ""))
                .collect(Collectors.joining("&"));
        return hmacSha256(checksumKey, canonicalData);
    }

    private static String hmacSha256(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException(PaymentMessages.PAYOS_SIGNATURE_GENERATION_FAILED, exception);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            result.append(String.format("%02x", value));
        }
        return result.toString();
    }

    private static byte[] hexToBytes(String value) {
        if (value.length() % 2 != 0 || !value.matches("[0-9a-fA-F]+")) {
            return new byte[0];
        }
        byte[] result = new byte[value.length() / 2];
        for (int index = 0; index < value.length(); index += 2) {
            result[index / 2] = (byte) Integer.parseInt(value.substring(index, index + 2), 16);
        }
        return result;
    }
}
