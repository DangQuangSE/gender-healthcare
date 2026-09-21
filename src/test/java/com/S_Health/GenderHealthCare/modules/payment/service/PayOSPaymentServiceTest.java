package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.integrations.payos.PayOSConfig;
import com.S_Health.GenderHealthCare.integrations.payos.PayOSGateway;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSPaymentStatusResponse;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PayOSPaymentServiceTest {

    @Mock private PaymentReservationService reservationService;
    @Mock private PaymentTransactionWriter transactionWriter;
    @Mock private PaymentFinalizationService finalizationService;
    @Mock private TransactionRepository transactionRepository;
    @Mock private PayOSGateway payOSGateway;
    @Mock private CurrentUserProvider currentUserProvider;

    private PayOSPaymentService service;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        PayOSConfig config = new PayOSConfig();
        config.setReturnUrl("https://app.test/return");
        config.setCancelUrl("https://app.test/cancel");
        service = new PayOSPaymentService(
                reservationService,
                transactionWriter,
                finalizationService,
                transactionRepository,
                payOSGateway,
                config,
                currentUserProvider);

        User customer = User.builder().id(7L).build();
        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        Payment payment = Payment.builder()
                .amount(new BigDecimal("100000"))
                .paymentIntent(PaymentIntent.DEPOSIT)
                .status(PaymentStatus.PENDING)
                .appointment(appointment)
                .build();
        transaction = Transaction.builder()
                .providerOrderCode(123L)
                .chargedAmount(new BigDecimal("20000"))
                .payment(payment)
                .build();
    }

    @Test
    void reusesProviderLinkFoundAfterAnUnknownCreateResponse() {
        when(reservationService.reserve(11L, PaymentIntent.DEPOSIT))
                .thenReturn(new PaymentReservation(transaction.getPayment(), transaction));
        when(payOSGateway.findPaymentStatus(123L)).thenReturn(Optional.of(new PayOSPaymentStatusResponse(
                123L, 20000L, 0L, 20000L, "link-123", "https://payos.test/123", "PENDING")));
        when(transactionWriter.attachPaymentLink(org.mockito.ArgumentMatchers.eq(123L), any()))
                .thenReturn(transaction);

        var response = service.createDepositPayment(11L);

        assertThat(response.orderCode()).isEqualTo(123L);
        verify(payOSGateway, never()).createPaymentLink(any());
        verify(transactionWriter).attachPaymentLink(org.mockito.ArgumentMatchers.eq(123L), any());
    }

    @Test
    void doesNotCallProviderForTerminalLocalStatus() {
        transaction.getPayment().setStatus(PaymentStatus.SUCCESS);
        when(transactionRepository.findByProviderOrderCode(123L)).thenReturn(Optional.of(transaction));
        when(currentUserProvider.requireUserId()).thenReturn(7L);

        var response = service.getStatus(123L);

        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        verify(payOSGateway, never()).getPaymentStatus(123L);
    }
}
