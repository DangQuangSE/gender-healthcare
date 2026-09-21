package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentMethod;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PaymentReservationService {

    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentAmountCalculator amountCalculator;
    private final CurrentUserProvider currentUserProvider;

    public PaymentReservationService(
            AppointmentRepository appointmentRepository,
            PaymentRepository paymentRepository,
            TransactionRepository transactionRepository,
            PaymentAmountCalculator amountCalculator,
            CurrentUserProvider currentUserProvider) {
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.amountCalculator = amountCalculator;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public PaymentReservation reserve(Long appointmentId, PaymentIntent paymentIntent) {
        if (paymentIntent == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_PAYMENT_REQUEST);
        }

        Appointment appointment = appointmentRepository.findByIdForPayment(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_NOT_FOUND));
        ensureAppointmentOwner(appointment);

        Payment existing = paymentRepository
                .findByAppointmentIdAndPaymentIntentAndStatus(appointmentId, paymentIntent, PaymentStatus.PENDING)
                .orElse(null);
        if (existing != null && existing.getTransaction() != null) {
            return new PaymentReservation(existing, existing.getTransaction());
        }

        Payment successful = paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.SUCCESS)
                .orElse(null);
        if (successful != null) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_ALREADY_PAID);
        }

        if (paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.FAILED).isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_CANCELLED);
        }

        if (appointment.getService() == null || appointment.getService().getPrice() == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYMENT_AMOUNT);
        }

        BigDecimal serviceAmount = BigDecimal.valueOf(appointment.getService().getPrice());
        BigDecimal chargedAmount = amountCalculator.calculate(serviceAmount, paymentIntent);
        Payment payment = paymentRepository.save(Payment.builder()
                .amount(serviceAmount)
                .paymentIntent(paymentIntent)
                .status(PaymentStatus.PENDING)
                .method(PaymentMethod.PAYOS)
                .appointment(appointment)
                .paidBy(appointment.getCustomer())
                .build());

        Transaction transaction = transactionRepository.save(Transaction.builder()
                .requestId(PaymentMessages.PAYOS_PENDING_TRANSACTION_REQUEST_ID)
                .chargedAmount(chargedAmount)
                .payment(payment)
                .build());
        transaction.setProviderOrderCode(transaction.getId());
        transaction.setRequestId(String.valueOf(transaction.getId()));
        transactionRepository.save(transaction);
        payment.setTransaction(transaction);
        return new PaymentReservation(payment, transaction);
    }

    private void ensureAppointmentOwner(Appointment appointment) {
        if (appointment.getCustomer() == null
                || !Objects.equals(appointment.getCustomer().getId(), currentUserProvider.requireUserId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, PaymentMessages.PAYMENT_APPOINTMENT_FORBIDDEN);
        }
    }
}
