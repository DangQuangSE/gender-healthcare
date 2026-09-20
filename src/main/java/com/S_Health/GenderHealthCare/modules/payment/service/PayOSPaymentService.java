package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.integrations.payos.PayOSConfig;
import com.S_Health.GenderHealthCare.integrations.payos.PayOSGateway;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkRequest;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentLinkResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentStatusResponse;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhook;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.PaymentLinkResponse;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.PaymentStatusResponse;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PayOSPaymentService {

    private final PaymentReservationService reservationService;
    private final PaymentTransactionWriter transactionWriter;
    private final PaymentFinalizationService finalizationService;
    private final TransactionRepository transactionRepository;
    private final PayOSGateway payOSGateway;
    private final PayOSConfig config;
    private final CurrentUserProvider currentUserProvider;

    public PayOSPaymentService(
            PaymentReservationService reservationService,
            PaymentTransactionWriter transactionWriter,
            PaymentFinalizationService finalizationService,
            TransactionRepository transactionRepository,
            PayOSGateway payOSGateway,
            PayOSConfig config,
            CurrentUserProvider currentUserProvider) {
        this.reservationService = reservationService;
        this.transactionWriter = transactionWriter;
        this.finalizationService = finalizationService;
        this.transactionRepository = transactionRepository;
        this.payOSGateway = payOSGateway;
        this.config = config;
        this.currentUserProvider = currentUserProvider;
    }

    public PaymentLinkResponse createFullPayment(Long appointmentId) {
        return createPayment(appointmentId, PaymentIntent.FULL);
    }

    public PaymentLinkResponse createDepositPayment(Long appointmentId) {
        return createPayment(appointmentId, PaymentIntent.DEPOSIT);
    }

    public PaymentStatusResponse getStatus(Long orderCode) {
        Transaction transaction = transactionRepository.findByProviderOrderCode(orderCode)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.TRANSACTION_NOT_FOUND));
        ensureOwner(transaction.getPayment());
        if (transaction.getPayment().getStatus() != PaymentStatus.PENDING) {
            return toStatusResponse(
                    transaction,
                    transaction.getResponseMessage(),
                    terminalMessage(transaction.getPayment().getStatus()));
        }
        PayOSPaymentStatusResponse providerStatus = payOSGateway.getPaymentStatus(orderCode);
        if (!Objects.equals(orderCode, providerStatus.orderCode())
                || (providerStatus.amount() != null
                && !Objects.equals(transaction.getChargedAmount().longValue(), providerStatus.amount()))) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }
        if (providerStatus.paymentLinkId() != null
                && transaction.getProviderPaymentLinkId() != null
                && !Objects.equals(providerStatus.paymentLinkId(), transaction.getProviderPaymentLinkId())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }
        return toStatusResponse(transaction, providerStatus.status(), PaymentMessages.PAYMENT_PENDING);
    }

    private PaymentStatusResponse toStatusResponse(
            Transaction transaction,
            String providerStatus,
            String message) {
        return new PaymentStatusResponse(
                transaction.getProviderOrderCode(),
                transaction.getPayment().getAmount().longValue(),
                transaction.getChargedAmount().longValue(),
                transaction.getProviderCheckoutUrl(),
                transaction.getProviderPaymentLinkId(),
                transaction.getPayment().getStatus(),
                transaction.getPayment().getPaymentIntent(),
                providerStatus,
                message);
    }

    public PaymentStatusResponse processWebhook(PayOSWebhook webhook) {
        PayOSWebhookData verifiedData = payOSGateway.verifyWebhook(webhook);
        return finalizationService.finalizeWebhook(verifiedData);
    }

    private PaymentLinkResponse createPayment(Long appointmentId, PaymentIntent paymentIntent) {
        PaymentReservation reservation = reservationService.reserve(appointmentId, paymentIntent);
        Transaction transaction = reservation.transaction();
        Payment payment = reservation.payment();
        if (transaction.getProviderCheckoutUrl() != null) {
            return toLinkResponse(transaction, payment);
        }

        Optional<PayOSPaymentStatusResponse> existingProviderLink =
                payOSGateway.findPaymentStatus(transaction.getProviderOrderCode());
        if (existingProviderLink.isPresent()) {
            PayOSPaymentStatusResponse providerStatus = existingProviderLink.get();
            validateProviderStatus(transaction, providerStatus);
            if (providerStatus.paymentLinkId() == null || providerStatus.checkoutUrl() == null) {
                throw new DomainException(ErrorCode.INTEGRATION_ERROR, PaymentMessages.PAYOS_PROVIDER_ERROR);
            }
            transaction = transactionWriter.attachPaymentLink(
                    transaction.getProviderOrderCode(),
                    new PayOSPaymentLinkResponse(
                            providerStatus.orderCode(),
                            providerStatus.amount(),
                            providerStatus.paymentLinkId(),
                            providerStatus.checkoutUrl(),
                            providerStatus.status()));
            return toLinkResponse(transaction, payment);
        }

        String description = buildDescription(transaction.getProviderOrderCode());
        PayOSPaymentLinkRequest request = new PayOSPaymentLinkRequest(
                transaction.getProviderOrderCode(),
                transaction.getChargedAmount().longValue(),
                description,
                config.getCancelUrl(),
                config.getReturnUrl());
        PayOSPaymentLinkResponse paymentLink = payOSGateway.createPaymentLink(request);
        transaction = transactionWriter.attachPaymentLink(transaction.getProviderOrderCode(), paymentLink);
        return toLinkResponse(transaction, payment);
    }

    private String buildDescription(Long orderCode) {
        String suffix = String.format("%07d", Math.floorMod(orderCode, 10_000_000));
        return PaymentMessages.PAYOS_DESCRIPTION_PREFIX + suffix;
    }

    private void ensureOwner(Payment payment) {
        if (payment.getAppointment().getCustomer() == null
                || !Objects.equals(
                payment.getAppointment().getCustomer().getId(),
                currentUserProvider.requireUserId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, PaymentMessages.PAYMENT_APPOINTMENT_FORBIDDEN);
        }
    }

    private void validateProviderStatus(Transaction transaction, PayOSPaymentStatusResponse providerStatus) {
        if (providerStatus == null
                || !Objects.equals(transaction.getProviderOrderCode(), providerStatus.orderCode())
                || providerStatus.amount() == null
                || transaction.getChargedAmount() == null
                || transaction.getChargedAmount().compareTo(java.math.BigDecimal.valueOf(providerStatus.amount())) != 0) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }
    }

    private String terminalMessage(PaymentStatus status) {
        return status == PaymentStatus.SUCCESS
                ? PaymentMessages.PAYMENT_SUCCESS
                : PaymentMessages.PAYMENT_FAILED;
    }

    private PaymentLinkResponse toLinkResponse(Transaction transaction, Payment payment) {
        return new PaymentLinkResponse(
                transaction.getProviderOrderCode(),
                payment.getAmount().longValue(),
                transaction.getChargedAmount().longValue(),
                transaction.getProviderCheckoutUrl(),
                transaction.getProviderPaymentLinkId(),
                payment.getStatus().name(),
                payment.getPaymentIntent());
    }
}
