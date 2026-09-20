package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentFinalizationServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private AppointmentRepository appointmentRepository;
    @Mock private AppointmentDetailRepository appointmentDetailRepository;

    private PaymentFinalizationService service;
    private Transaction transaction;
    private Payment payment;
    private Appointment appointment;
    private AppointmentDetail detail;

    @BeforeEach
    void setUp() {
        service = new PaymentFinalizationService(
                transactionRepository,
                paymentRepository,
                appointmentRepository,
                appointmentDetailRepository);

        appointment = new Appointment();
        appointment.setId(7L);
        appointment.setService(Service.builder().isCombo(false).build());
        detail = AppointmentDetail.builder().appointment(appointment).status(AppointmentStatus.PENDING).build();
        payment = Payment.builder()
                .amount(new BigDecimal("100000"))
                .paymentIntent(PaymentIntent.DEPOSIT)
                .status(PaymentStatus.PENDING)
                .appointment(appointment)
                .build();
        transaction = Transaction.builder()
                .providerOrderCode(123L)
                .providerPaymentLinkId("link-123")
                .chargedAmount(new BigDecimal("20000"))
                .payment(payment)
                .build();
        when(transactionRepository.findByProviderOrderCodeForUpdate(123L)).thenReturn(Optional.of(transaction));
    }

    @Test
    void rejectsAmountMismatchBeforeMutatingPaymentOrAppointment() {
        PayOSWebhookData data = webhook(20001L, "00", "link-123");

        DomainException exception = assertThrows(DomainException.class, () -> service.finalizeWebhook(data));

        assertThat(exception.getMessage()).isEqualTo(PaymentMessages.PAYMENT_AMOUNT_MISMATCH);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        verify(appointmentRepository, never()).save(appointment);
        verify(paymentRepository, never()).save(payment);
    }

    @Test
    void duplicateSuccessfulWebhookIsIdempotentAndDoesNotTouchAppointmentAgain() {
        payment.setStatus(PaymentStatus.SUCCESS);

        var response = service.finalizeWebhook(webhook(20000L, "00", "link-123"));

        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        verify(appointmentRepository, never()).save(appointment);
        verify(paymentRepository, never()).save(payment);
    }

    @Test
    void successfulWebhookUpdatesAppointmentAndDetailOnce() {
        when(appointmentDetailRepository.findByAppointmentId(7L)).thenReturn(Optional.of(detail));

        var response = service.finalizeWebhook(webhook(20000L, "00", "link-123"));

        assertThat(response.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        assertThat(detail.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
        verify(appointmentRepository).save(appointment);
        verify(appointmentDetailRepository).save(detail);
    }

    private PayOSWebhookData webhook(Long amount, String code, String paymentLinkId) {
        return new PayOSWebhookData(
                123L,
                amount,
                "SH0000123",
                null,
                "reference-123",
                "2026-09-20 12:00:00",
                "VND",
                paymentLinkId,
                code,
                "success",
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
