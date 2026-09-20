package com.S_Health.GenderHealthCare.integration;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.integrations.vnpay.VNPayCallback;
import com.S_Health.GenderHealthCare.integrations.vnpay.VNPayGatewayAdapter;
import com.S_Health.GenderHealthCare.modules.payment.config.VNPayConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VNPayGatewayAdapterTest {
    private static final String HASH_SECRET = "test-vnpay-secret";
    private VNPayGatewayAdapter adapter;

    @BeforeEach
    void setUp() {
        VNPayConfig config = new VNPayConfig();
        config.setHashSecret(HASH_SECRET);
        config.setPayUrl("https://sandbox.vnpay.test/pay");
        config.setReturnUrl("http://localhost:5173/payment/return");
        config.setTmnCode("TEST1234");
        adapter = new VNPayGatewayAdapter(config);
    }

    @Test
    void verifiesCallbackWhenSignatureUsesSortedEncodedParameters() {
        MockHttpServletRequest request = signedRequest(Map.of(
                "vnp_Amount", "1000000",
                "vnp_ResponseCode", "00",
                "vnp_TxnRef", "order-001",
                "vnp_TransactionNo", "987654",
                "vnp_TransactionStatus", "00",
                "vnp_PayDate", "20260920120000"));

        VNPayCallback callback = adapter.verifyCallback(request);

        assertThat(callback.transactionReference()).isEqualTo("order-001");
        assertThat(callback.amount()).isEqualByComparingTo("10000");
        assertThat(callback.resultCode()).isZero();
    }

    @Test
    void rejectsCallbackWhenSignatureDoesNotMatchPayload() {
        MockHttpServletRequest request = signedRequest(Map.of(
                "vnp_Amount", "1000000",
                "vnp_ResponseCode", "00",
                "vnp_TxnRef", "order-001"));
        request.setParameter("vnp_ResponseCode", "24");

        assertThrows(DomainException.class, () -> adapter.verifyCallback(request));
    }

    @Test
    void rejectsCallbackWithoutAmountEvenWhenSignatureIsValid() {
        MockHttpServletRequest request = signedRequest(Map.of(
                "vnp_ResponseCode", "00",
                "vnp_TxnRef", "order-001"));

        assertThrows(DomainException.class, () -> adapter.verifyCallback(request));
    }

    private MockHttpServletRequest signedRequest(Map<String, String> source) {
        Map<String, String> parameters = new TreeMap<>(source);
        StringBuilder hashData = new StringBuilder();
        parameters.forEach((key, value) -> {
            if (!hashData.isEmpty()) {
                hashData.append('&');
            }
            hashData.append(key)
                    .append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
        });
        MockHttpServletRequest request = new MockHttpServletRequest();
        parameters.forEach(request::addParameter);
        request.addParameter(
                "vnp_SecureHash",
                VNPayConfig.hmacSHA512(HASH_SECRET, hashData.toString()));
        request.addParameter("vnp_SecureHashType", "HmacSHA512");
        return request;
    }
}
