package com.S_Health.GenderHealthCare.modules.payment.controller;

import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhook;
import com.S_Health.GenderHealthCare.modules.payment.dto.request.PaymentRequest;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.PaymentLinkResponse;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.PaymentStatusResponse;
import com.S_Health.GenderHealthCare.modules.payment.service.PayOSPaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/payos")
public class PayOSPaymentController {

    private final PayOSPaymentService paymentService;

    public PayOSPaymentController(PayOSPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentLinkResponse createPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createFullPayment(request.getAppointmentId());
    }

    @PostMapping("/deposit")
    public PaymentLinkResponse createDepositPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createDepositPayment(request.getAppointmentId());
    }

    @GetMapping("/{orderCode}")
    public PaymentStatusResponse getStatus(@PathVariable Long orderCode) {
        return paymentService.getStatus(orderCode);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody PayOSWebhook webhook) {
        paymentService.processWebhook(webhook);
        return ResponseEntity.ok().build();
    }
}
