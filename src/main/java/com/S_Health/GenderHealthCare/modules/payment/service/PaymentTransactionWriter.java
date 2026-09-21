package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkResponse;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PaymentTransactionWriter {

    private final TransactionRepository transactionRepository;

    public PaymentTransactionWriter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction attachPaymentLink(Long orderCode, PayOSPaymentLinkResponse paymentLink) {
        if (paymentLink == null
                || paymentLink.orderCode() == null
                || paymentLink.amount() == null
                || !Objects.equals(orderCode, paymentLink.orderCode())
                || !hasText(paymentLink.paymentLinkId())
                || !hasText(paymentLink.checkoutUrl())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }

        Transaction transaction = transactionRepository.findByProviderOrderCodeForUpdate(orderCode)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.TRANSACTION_NOT_FOUND));
        if (transaction.getChargedAmount() == null
                || transaction.getProviderOrderCode() == null
                || transaction.getChargedAmount().compareTo(java.math.BigDecimal.valueOf(paymentLink.amount())) != 0
                || !Objects.equals(transaction.getProviderOrderCode(), paymentLink.orderCode())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_AMOUNT_MISMATCH);
        }

        transaction.setProviderPaymentLinkId(paymentLink.paymentLinkId());
        transaction.setProviderCheckoutUrl(paymentLink.checkoutUrl());
        transaction.setResponseMessage(paymentLink.status());
        return transactionRepository.save(transaction);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
