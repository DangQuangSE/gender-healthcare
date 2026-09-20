package com.S_Health.GenderHealthCare.integrations.payos;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkRequest;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhook;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PayOSGatewayAdapterTest {

    private PayOSConfig config;
    private MockRestServiceServer server;
    private PayOSGatewayAdapter adapter;

    @BeforeEach
    void setUp() {
        config = new PayOSConfig();
        config.setClientId("client-id");
        config.setApiKey("api-key");
        config.setChecksumKey("checksum-key");
        config.setApiBaseUrl("https://api.payos.test");
        config.setReturnUrl("https://app.test/payment/return");
        config.setCancelUrl("https://app.test/payment/cancel");
        config.setWebhookUrl("https://api.test/api/v1/payments/payos/webhook");

        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        adapter = new PayOSGatewayAdapter(builder.baseUrl(config.getApiBaseUrl()).build(), config);
    }

    @Test
    void mapsProviderCreateResponseAndSendsPayOSHeadersAndSignature() {
        server.expect(requestTo("https://api.payos.test/v2/payment-requests"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-client-id", "client-id"))
                .andExpect(header("x-api-key", "api-key"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"orderCode\":123")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"amount\":2000")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("\"signature\":\"")))
                .andRespond(withSuccess("""
                        {
                          "code":"00",
                          "data":{
                            "orderCode":123,
                            "amount":2000,
                            "paymentLinkId":"link-123",
                            "checkoutUrl":"https://payos.test/checkout/123",
                            "status":"PENDING"
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        var response = adapter.createPaymentLink(new PayOSPaymentLinkRequest(
                123L, 2000L, "SH0000123", config.getCancelUrl(), config.getReturnUrl()));

        assertThat(response.orderCode()).isEqualTo(123L);
        assertThat(response.amount()).isEqualTo(2000L);
        assertThat(response.paymentLinkId()).isEqualTo("link-123");
        server.verify();
    }

    @Test
    void rejectsProviderCreateResponseWhenOrderOrAmountDoesNotMatch() {
        server.expect(requestTo("https://api.payos.test/v2/payment-requests"))
                .andRespond(withSuccess("""
                        {"code":"00","data":{"orderCode":999,"amount":2000,
                        "paymentLinkId":"link-999","checkoutUrl":"https://payos.test/999",
                        "status":"PENDING"}}
                        """, MediaType.APPLICATION_JSON));

        assertThrows(DomainException.class, () -> adapter.createPaymentLink(new PayOSPaymentLinkRequest(
                123L, 2000L, "SH0000123", config.getCancelUrl(), config.getReturnUrl())));
    }

    @Test
    void treatsProviderNotFoundAsRecoverableLookupMiss() {
        server.expect(requestTo("https://api.payos.test/v2/payment-requests/123"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound());

        Optional<?> result = adapter.findPaymentStatus(123L);

        assertThat(result).isEmpty();
        server.verify();
    }

    @Test
    void rejectsWebhookWhenProviderEnvelopeIsNotSuccessful() {
        PayOSWebhookData data = new PayOSWebhookData(
                123L, 2000L, "SH0000123", null, null, null, "VND", "link-123", "00", "success",
                null, null, null, null, null, null);

        assertThrows(DomainException.class, () -> adapter.verifyWebhook(
                new PayOSWebhook("01", "provider error", false, data, "invalid")));
    }
}
