package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentMethod;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentReservationServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private PaymentAmountCalculator amountCalculator;
    @Mock private CurrentUserProvider currentUserProvider;

    private PaymentReservationService service;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        service = new PaymentReservationService(
                appointmentRepository,
                paymentRepository,
                transactionRepository,
                amountCalculator,
                currentUserProvider);

        User customer = User.builder().id(7L).build();
        appointment = new Appointment();
        appointment.setId(11L);
        appointment.setCustomer(customer);
        appointment.setService(Service.builder().price(100001D).build());

        when(appointmentRepository.findByIdForPayment(11L)).thenReturn(Optional.of(appointment));
        when(currentUserProvider.requireUserId()).thenReturn(7L);
        when(paymentRepository.findByAppointmentIdAndPaymentIntentAndStatus(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(paymentRepository.findByAppointmentIdAndStatus(any(), any())).thenReturn(Optional.empty());
        when(amountCalculator.calculate(any(), any())).thenReturn(new java.math.BigDecimal("20000"));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            if (transaction.getId() == null) {
                transaction.setId(99L);
            }
            return transaction;
        });
    }

    @Test
    void reservesPayOSDepositWithActualChargedAmountAndFullServiceAmount() {
        PaymentReservation reservation = service.reserve(11L, PaymentIntent.DEPOSIT);

        assertThat(reservation.payment().getAmount()).isEqualByComparingTo("100001");
        assertThat(reservation.payment().getPaymentIntent()).isEqualTo(PaymentIntent.DEPOSIT);
        assertThat(reservation.payment().getMethod()).isEqualTo(PaymentMethod.PAYOS);
        assertThat(reservation.transaction().getChargedAmount()).isEqualByComparingTo("20000");
        assertThat(reservation.transaction().getProviderOrderCode()).isEqualTo(99L);
    }
}
