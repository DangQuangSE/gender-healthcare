package com.S_Health.GenderHealthCare.modules.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String transactionCode;
    String requestId;
    String orderId;
    @Column(unique = true)
    Long providerOrderCode;
    @Column(length = 128)
    String providerPaymentLinkId;
    @Column(length = 512)
    String providerCheckoutUrl;
    @Column(precision = 19, scale = 0)
    BigDecimal chargedAmount;
    String responseMessage;
    int resultCode;
//    @Column(name = "pay_url", columnDefinition = "TEXT")
//    String payUrl;
    LocalDateTime responseTime;


    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    Payment payment;
}
