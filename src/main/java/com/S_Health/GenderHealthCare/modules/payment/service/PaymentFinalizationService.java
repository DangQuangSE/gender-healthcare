package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.payos.dto.PayOSWebhookData;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence.AppointmentRepository;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.PaymentStatusResponse;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentFinalizationService {

    private static final DateTimeFormatter PAYOS_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;

    public PaymentFinalizationService(
            TransactionRepository transactionRepository,
            PaymentRepository paymentRepository,
            AppointmentRepository appointmentRepository,
            AppointmentDetailRepository appointmentDetailRepository) {
        this.transactionRepository = transactionRepository;
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
    }

    @Transactional
    public PaymentStatusResponse finalizeWebhook(PayOSWebhookData data) {
        if (data == null || data.orderCode() == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_WEBHOOK);
        }
        Transaction transaction = transactionRepository.findByProviderOrderCodeForUpdate(data.orderCode())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.TRANSACTION_NOT_FOUND));
        Payment payment = transaction.getPayment();

        validateProviderData(transaction, data);
        if (payment.getStatus() != PaymentStatus.PENDING) {
            return toResponse(transaction, payment);
        }

        boolean successful = "00".equals(data.code());
        payment.setStatus(successful ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        payment.setPaidAt(parsePaymentTime(data.transactionDateTime()));
        transaction.setTransactionCode(data.reference());
        transaction.setResponseMessage(data.desc());
        transaction.setResultCode(parseResultCode(data.code()));
        transaction.setResponseTime(LocalDateTime.now());

        updateAppointmentState(payment.getAppointment(), successful ? AppointmentStatus.CONFIRMED : AppointmentStatus.CANCELED);
        paymentRepository.save(payment);
        transactionRepository.save(transaction);
        return toResponse(transaction, payment);
    }

    private void validateProviderData(Transaction transaction, PayOSWebhookData data) {
        if (data == null
                || data.orderCode() == null
                || !Objects.equals(transaction.getProviderOrderCode(), data.orderCode())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }
        if (data.amount() == null
                || transaction.getChargedAmount() == null
                || transaction.getChargedAmount().compareTo(BigDecimal.valueOf(data.amount())) != 0) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_AMOUNT_MISMATCH);
        }
        if (transaction.getProviderPaymentLinkId() != null
                && !Objects.equals(transaction.getProviderPaymentLinkId(), data.paymentLinkId())) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.PAYMENT_ORDER_MISMATCH);
        }
        if (data.code() == null || data.code().isBlank()) {
            throw new DomainException(ErrorCode.BAD_REQUEST, PaymentMessages.INVALID_PAYOS_WEBHOOK);
        }
    }

    private void updateAppointmentState(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
        if (Boolean.TRUE.equals(appointment.getService().getIsCombo())) {
            List<AppointmentDetail> details = appointmentDetailRepository.findAllAppointmentDetails(appointment.getId());
            if (details.isEmpty()) {
                throw new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND);
            }
            details.forEach(detail -> detail.setStatus(status));
            appointmentDetailRepository.saveAll(details);
            return;
        }

        AppointmentDetail detail = appointmentDetailRepository.findByAppointmentId(appointment.getId())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND));
        detail.setStatus(status);
        appointmentDetailRepository.save(detail);
    }

    private LocalDateTime parsePaymentTime(String value) {
        if (value == null || value.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(value, PAYOS_DATE_TIME);
        } catch (DateTimeParseException exception) {
            return LocalDateTime.now();
        }
    }

    private int parseResultCode(String code) {
        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private PaymentStatusResponse toResponse(Transaction transaction, Payment payment) {
        return new PaymentStatusResponse(
                transaction.getProviderOrderCode(),
                payment.getAmount().longValue(),
                transaction.getChargedAmount().longValue(),
                transaction.getProviderCheckoutUrl(),
                transaction.getProviderPaymentLinkId(),
                payment.getStatus(),
                payment.getPaymentIntent(),
                transaction.getResponseMessage(),
                payment.getStatus() == PaymentStatus.SUCCESS
                        ? PaymentMessages.PAYMENT_SUCCESS
                        : PaymentMessages.PAYMENT_FAILED);
    }
}
