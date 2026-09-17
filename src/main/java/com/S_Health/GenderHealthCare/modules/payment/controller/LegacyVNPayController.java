package com.S_Health.GenderHealthCare.modules.payment.controller;

import com.S_Health.GenderHealthCare.modules.payment.dto.response.VNPayResponse;
import com.S_Health.GenderHealthCare.modules.payment.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/vnpay")
public class LegacyVNPayController {
    private final VNPayService vnPayService;

    public LegacyVNPayController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @GetMapping("/create")
    public ResponseEntity<VNPayResponse> createPayment(@RequestParam long appointmentId,
                                                       HttpServletRequest request) {
        VNPayResponse response = vnPayService.createOrder(appointmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/create-off")
    public ResponseEntity<VNPayResponse> createPaymentOff(@RequestParam long appointmentId,
                                                       HttpServletRequest request) {
        VNPayResponse response = vnPayService.createOrderOff(appointmentId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/vnpay-return")
    public ResponseEntity<VNPayResponse> handleReturn(HttpServletRequest request) {
        return ResponseEntity.ok(vnPayService.processReturn(request));
    }


}
