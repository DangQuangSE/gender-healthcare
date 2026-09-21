package com.S_Health.GenderHealthCare.integrations.payos;

import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkRequest;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentStatusResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhook;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;

import java.util.Optional;

public interface PayOSGateway {
    PayOSPaymentLinkResponse createPaymentLink(PayOSPaymentLinkRequest request);

    PayOSPaymentStatusResponse getPaymentStatus(Long orderCode);

    Optional<PayOSPaymentStatusResponse> findPaymentStatus(Long orderCode);

    PayOSWebhookData verifyWebhook(PayOSWebhook webhook);
}
