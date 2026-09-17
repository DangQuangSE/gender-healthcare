package com.S_Health.GenderHealthCare.modules.payment.controller;

import com.S_Health.GenderHealthCare.dto.response.payment.VNPayResponse;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.service.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments/vnpay")
public class VNPayController {
    private final VNPayService vnPayService;

    public VNPayController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @GetMapping
    @Operation(summary = PaymentMessages.CREATE_VNPAY_PAYMENT)
    public VNPayResponse createPayment(@RequestParam long appointmentId) {
        return vnPayService.createOrder(appointmentId);
    }

    @GetMapping("/offline")
    @Operation(summary = PaymentMessages.CREATE_OFFLINE_PAYMENT)
    public VNPayResponse createOfflinePayment(@RequestParam long appointmentId) {
        return vnPayService.createOrderOff(appointmentId);
    }

    @GetMapping("/return")
    @Operation(summary = PaymentMessages.HANDLE_VNPAY_RETURN)
    public ResponseEntity<VNPayResponse> handleReturn(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(vnPayService.processReturn(request));
        } catch (AppException e) {
            return ResponseEntity.badRequest()
                    .body(VNPayResponse.builder().message(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(VNPayResponse.builder()
                            .message(PaymentMessages.PAYMENT_PROCESSING_FAILED)
                            .build());
        }
    }
}
