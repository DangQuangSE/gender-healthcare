package com.S_Health.GenderHealthCare.integrations.vnpay;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

/**
 * Provider boundary used by the payment module.
 */
public interface VNPayGateway {
    VNPayPaymentLink createPaymentLink(
            BigDecimal amount,
            String orderInfo,
            int timeoutMinutes);

    VNPayCallback verifyCallback(HttpServletRequest request);
}
