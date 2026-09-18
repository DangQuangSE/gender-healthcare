package com.S_Health.GenderHealthCare.integrations.vnpay;

import java.time.LocalDateTime;

/**
 * Verified callback data received from VNPay.
 */
public record VNPayCallback(
        String transactionReference,
        String transactionNumber,
        String transactionStatus,
        int resultCode,
        LocalDateTime paymentTime) {
}
