package com.S_Health.GenderHealthCare.modules.payment.config;

import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Configuration and small utilities required by the VNPay module.
 */
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VNPayConfig {
    private String payUrl;
    private String returnUrl;
    private String tmnCode;
    private String hashSecret;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getTmnCode() {
        return tmnCode;
    }

    public void setTmnCode(String tmnCode) {
        this.tmnCode = tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public void setHashSecret(String hashSecret) {
        this.hashSecret = hashSecret;
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA512");
            hmac512.init(secretKeySpec);
            return bytesToHex(hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException(PaymentMessages.VNPAY_SIGNATURE_GENERATION_FAILED, exception);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            result.append(String.format("%02x", value));
        }
        return result.toString();
    }
}
