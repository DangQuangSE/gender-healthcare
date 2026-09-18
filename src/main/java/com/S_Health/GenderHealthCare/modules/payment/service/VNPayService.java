package com.S_Health.GenderHealthCare.modules.payment.service;

import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.payment.dto.response.VNPayResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import com.S_Health.GenderHealthCare.integrations.vnpay.VNPayCallback;
import com.S_Health.GenderHealthCare.integrations.vnpay.VNPayGateway;
import com.S_Health.GenderHealthCare.integrations.vnpay.VNPayPaymentLink;
import com.S_Health.GenderHealthCare.repository.AppointmentDetailRepository;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import com.S_Health.GenderHealthCare.repository.PaymentRepository;
import com.S_Health.GenderHealthCare.repository.TransactionRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import static com.S_Health.GenderHealthCare.modules.payment.enums.PaymentMethod.PAY_OFF;
import static com.S_Health.GenderHealthCare.modules.payment.enums.PaymentMethod.VN_PAY;

@Service
public class VNPayService {
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final VNPayGateway vnPayGateway;
    private final AuthUtil authUtil;

    public VNPayService(
            AppointmentRepository appointmentRepository,
            PaymentRepository paymentRepository,
            TransactionRepository transactionRepository,
            AppointmentDetailRepository appointmentDetailRepository,
            VNPayGateway vnPayGateway,
            AuthUtil authUtil) {
        this.appointmentRepository = appointmentRepository;
        this.paymentRepository = paymentRepository;
        this.transactionRepository = transactionRepository;
        this.appointmentDetailRepository = appointmentDetailRepository;
        this.vnPayGateway = vnPayGateway;
        this.authUtil = authUtil;
    }


    @Transactional
    public VNPayResponse createOrder(Long appointmentId){

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_NOT_FOUND));

        ensureAppointmentOwner(appointment);

        Optional<Payment> paid = paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.SUCCESS);
        if (paid.isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_ALREADY_PAID);
        }

        // Tìm giao dịch thanh toán thất bại
        Optional<Payment> failed = paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.FAILED);
        if (failed.isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_CANCELLED);
        }

        BigDecimal price = BigDecimal.valueOf(appointment.getService().getPrice());
        long amount = price.longValue();
        String orderInfo = PaymentMessages.ORDER_INFO_FORMAT.formatted(appointment.getService().getName());
        VNPayPaymentLink paymentLink = vnPayGateway.createPaymentLink(
                BigDecimal.valueOf(amount),
                orderInfo,
                PaymentMessages.VNPAY_TIMEOUT_MINUTES);

        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(amount))
                .status(PaymentStatus.PENDING)
                .method(VN_PAY)
                .appointment(appointment)
                .paidBy(appointment.getCustomer())
                .build();
        payment = paymentRepository.save(payment);

        Transaction transaction = Transaction.builder()
                .orderId(paymentLink.transactionReference())
                .requestId(paymentLink.transactionReference())
                .payment(payment)
                .build();
        transactionRepository.save(transaction);

        return VNPayResponse.builder()
                .amount(amount)
                .URL(paymentLink.url())
                .build();

    }

    private void ensureAppointmentOwner(Appointment appointment) {
        if (appointment.getCustomer() == null
                || !Objects.equals(appointment.getCustomer().getId(), authUtil.getCurrentUserId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, PaymentMessages.PAYMENT_APPOINTMENT_FORBIDDEN);
        }
    }

    @Transactional
    public VNPayResponse createOrderOff(Long appointmentId){

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_NOT_FOUND));

        ensureAppointmentOwner(appointment);

        Optional<Payment> paid = paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.SUCCESS);
        if (paid.isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_ALREADY_PAID);
        }

        // Tìm giao dịch thanh toán thất bại
        Optional<Payment> failed = paymentRepository.findByAppointmentIdAndStatus(appointmentId, PaymentStatus.FAILED);
        if (failed.isPresent()) {
            throw new DomainException(ErrorCode.CONFLICT, PaymentMessages.APPOINTMENT_CANCELLED);
        }

        BigDecimal price = BigDecimal.valueOf(appointment.getService().getPrice());
        BigDecimal payAmount = price.multiply(PaymentMessages.OFFLINE_PAYMENT_RATE);
        long amount = price.longValue();
        String orderInfo = PaymentMessages.ORDER_INFO_FORMAT.formatted(appointment.getService().getName());
        VNPayPaymentLink paymentLink = vnPayGateway.createPaymentLink(
                payAmount,
                orderInfo,
                PaymentMessages.OFFLINE_PAYMENT_TIMEOUT_MINUTES);

        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(amount))
                .status(PaymentStatus.PENDING)
                .method(PAY_OFF)
                .appointment(appointment)
                .paidBy(appointment.getCustomer())
                .build();
        payment = paymentRepository.save(payment);

        Transaction transaction = Transaction.builder()
                .orderId(paymentLink.transactionReference())
                .requestId(paymentLink.transactionReference())
                .payment(payment)
                .build();
        transactionRepository.save(transaction);

        return VNPayResponse.builder()
                .amount(amount)
                .URL(paymentLink.url())
                .build();

    }
    @Transactional
    public VNPayResponse processReturn(HttpServletRequest request) {
        VNPayCallback callback = vnPayGateway.verifyCallback(request);

        Transaction transaction = transactionRepository.findByOrderId(callback.transactionReference())
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.TRANSACTION_NOT_FOUND));

        Payment payment = transaction.getPayment();

        if (callback.resultCode() == 0) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(callback.paymentTime());
            Appointment appointment = payment.getAppointment();
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);


            if(appointment.getService().getIsCombo()){
                List<AppointmentDetail> appointmentDetails = appointmentDetailRepository.findAllAppointmentDetails(appointment.getId());
                if(appointmentDetails.isEmpty()){
                    throw new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND);
                }
                for(AppointmentDetail appointmentDetail : appointmentDetails){
                    appointmentDetail.setStatus(AppointmentStatus.CONFIRMED);
                }
                appointmentDetailRepository.saveAll(appointmentDetails);
            } else {
                AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentId(appointment.getId())
                        .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND));
                appointmentDetail.setStatus(AppointmentStatus.CONFIRMED);
                appointmentDetailRepository.save(appointmentDetail);
            }

        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaidAt(LocalDateTime.now());
            Appointment appointment = payment.getAppointment();
            appointment.setStatus(AppointmentStatus.CANCELED);
            appointmentRepository.save(appointment);

            if(appointment.getService().getIsCombo()){
                List<AppointmentDetail> appointmentDetails = appointmentDetailRepository.findAllAppointmentDetails(appointment.getId());
                if(appointmentDetails.isEmpty()){
                    throw new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND);
                }
                for(AppointmentDetail appointmentDetail : appointmentDetails){
                    appointmentDetail.setStatus(AppointmentStatus.CANCELED);
                }
                appointmentDetailRepository.saveAll(appointmentDetails);
            } else {
                AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentId(appointment.getId())
                        .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, PaymentMessages.APPOINTMENT_DETAIL_NOT_FOUND));
                appointmentDetail.setStatus(AppointmentStatus.CANCELED);
                appointmentDetailRepository.save(appointmentDetail);
            }

        }

        paymentRepository.save(payment);

        transaction.setTransactionCode(callback.transactionNumber());
        transaction.setResultCode(callback.resultCode());
        transaction.setResponseMessage(callback.transactionStatus());
        transaction.setResponseTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        return VNPayResponse.builder()
                .amount(payment.getAmount().longValue())
                .URL(null)
                .message(payment.getStatus() == PaymentStatus.SUCCESS
                        ? PaymentMessages.PAYMENT_SUCCESS
                        : PaymentMessages.PAYMENT_FAILED)
                .build();
    }



}
