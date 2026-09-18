package com.S_Health.GenderHealthCare.integrations.vnpay;

/**
 * Payment link returned by the VNPay provider adapter.
 */
public record VNPayPaymentLink(
        String transactionReference,
        String url) {
}
