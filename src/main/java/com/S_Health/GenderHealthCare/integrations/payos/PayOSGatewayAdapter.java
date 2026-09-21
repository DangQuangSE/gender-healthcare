package com.S_Health.GenderHealthCare.integrations.payos;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSCreatePaymentPayload;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkRequest;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentStatusResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSProviderData;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSProviderResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhook;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PayOSGatewayAdapter implements PayOSGateway {

    private static final String SUCCESS_CODE = "00";
    private static final int DESCRIPTION_MAX_LENGTH = 9;

    private final RestClient restClient;
    private final PayOSConfig config;
    private final PayOSSignatureVerifier signatureVerifier;

    public PayOSGatewayAdapter(
            RestClient payOSRestClient,
            PayOSConfig config) {
        this.restClient = payOSRestClient;
        this.config = config;
        this.signatureVerifier = new PayOSSignatureVerifier(config.getChecksumKey());
    }

    @Override
    public PayOSPaymentLinkResponse createPaymentLink(PayOSPaymentLinkRequest request) {
        validatePaymentLinkRequest(request);
        String signature = signatureVerifier.createPaymentSignature(
                request.amount(),
                request.cancelUrl(),
                request.description(),
                request.orderCode(),
                request.returnUrl());
        PayOSCreatePaymentPayload payload = new PayOSCreatePaymentPayload(
                request.orderCode(),
                request.amount(),
                request.description(),
                request.cancelUrl(),
                request.returnUrl(),
                signature);

        PayOSProviderResponse response = execute(() -> restClient.post()
                .uri("/v2/payment-requests")
                .header("x-client-id", config.getClientId())
                .header("x-api-key", config.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(payload)
                .retrieve()
                .body(PayOSProviderResponse.class));
        PayOSProviderData data = requireSuccessfulData(response);
        validateReturnedPaymentLink(data, request);
        return new PayOSPaymentLinkResponse(
                data.orderCode(),
                data.amount(),
                data.paymentLinkId(),
                data.checkoutUrl(),
                data.status());
    }

    @Override
    public PayOSPaymentStatusResponse getPaymentStatus(Long orderCode) {
        if (orderCode == null || orderCode <= 0) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_ORDER_CODE);
        }

        PayOSProviderResponse response = execute(() -> restClient.get()
                .uri("/v2/payment-requests/{orderCode}", orderCode)
                .header("x-client-id", config.getClientId())
                .header("x-api-key", config.getApiKey())
                .retrieve()
                .body(PayOSProviderResponse.class));
        PayOSProviderData data = requireSuccessfulData(response);
        return new PayOSPaymentStatusResponse(
                data.orderCode(),
                data.amount(),
                data.amountPaid(),
                data.amountRemaining(),
                data.paymentLinkId(),
                data.checkoutUrl(),
                data.status());
    }

    @Override
    public Optional<PayOSPaymentStatusResponse> findPaymentStatus(Long orderCode) {
        try {
            return Optional.of(getPaymentStatus(orderCode));
        } catch (DomainException exception) {
            if (exception.getCause() instanceof HttpClientErrorException.NotFound) {
                return Optional.empty();
            }
            throw exception;
        }
    }

    @Override
    public PayOSWebhookData verifyWebhook(PayOSWebhook webhook) {
        if (webhook == null
                || !webhook.success()
                || !SUCCESS_CODE.equals(webhook.code())
                || webhook.data() == null
                || !signatureVerifier.verifyWebhookSignature(webhookData(webhook.data()), webhook.signature())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_WEBHOOK);
        }
        if (webhook.data().orderCode() == null
                || webhook.data().orderCode() <= 0
                || webhook.data().amount() == null
                || webhook.data().amount() <= 0
                || !StringUtils.hasText(webhook.data().paymentLinkId())
                || !StringUtils.hasText(webhook.data().code())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_WEBHOOK);
        }
        return webhook.data();
    }

    private void validatePaymentLinkRequest(PayOSPaymentLinkRequest request) {
        if (request == null
                || request.orderCode() == null
                || request.orderCode() <= 0
                || request.amount() == null
                || request.amount() <= 0
                || !StringUtils.hasText(request.description())
                || request.description().length() > DESCRIPTION_MAX_LENGTH
                || !StringUtils.hasText(request.cancelUrl())
                || !StringUtils.hasText(request.returnUrl())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_PAYMENT_REQUEST);
        }
    }

    private PayOSProviderData requireSuccessfulData(PayOSProviderResponse response) {
        if (response == null || !SUCCESS_CODE.equals(response.code()) || response.data() == null) {
            throw new DomainException(ErrorCode.INTEGRATION_ERROR, PaymentMessages.PAYOS_PROVIDER_ERROR);
        }
        return response.data();
    }

    private void validateReturnedPaymentLink(
            PayOSProviderData data,
            PayOSPaymentLinkRequest request) {
        if (!request.orderCode().equals(data.orderCode())
                || !request.amount().equals(data.amount())
                || !StringUtils.hasText(data.paymentLinkId())
                || !StringUtils.hasText(data.checkoutUrl())) {
            throw new DomainException(ErrorCode.INTEGRATION_ERROR, PaymentMessages.PAYOS_PROVIDER_ERROR);
        }
    }

    private <T> T execute(java.util.function.Supplier<T> request) {
        try {
            return request.get();
        } catch (RestClientException exception) {
            throw new DomainException(ErrorCode.INTEGRATION_ERROR, PaymentMessages.PAYOS_PROVIDER_ERROR, exception);
        }
    }

    private Map<String, Object> webhookData(PayOSWebhookData data) {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("accountNumber", data.accountNumber());
        fields.put("amount", data.amount());
        fields.put("counterAccountBankId", data.counterAccountBankId());
        fields.put("counterAccountBankName", data.counterAccountBankName());
        fields.put("counterAccountName", data.counterAccountName());
        fields.put("counterAccountNumber", data.counterAccountNumber());
        fields.put("currency", data.currency());
        fields.put("desc", data.desc());
        fields.put("description", data.description());
        fields.put("orderCode", data.orderCode());
        fields.put("paymentLinkId", data.paymentLinkId());
        fields.put("reference", data.reference());
        fields.put("transactionDateTime", data.transactionDateTime());
        fields.put("virtualAccountName", data.virtualAccountName());
        fields.put("virtualAccountNumber", data.virtualAccountNumber());
        fields.put("code", data.code());
        return fields;
    }
}
