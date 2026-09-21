package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;

public record PaymentReservation(Payment payment, Transaction transaction) {
}
