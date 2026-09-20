package com.S_Health.GenderHealthCare.modules.payment;

import java.math.BigDecimal;

/**
 * Messages and fixed provider values used by payment use cases.
 */
public final class PaymentMessages {
    public static final String APPOINTMENT_NOT_FOUND = "Cuộc hẹn không tồn tại";
    public static final String APPOINTMENT_ALREADY_PAID = "Cuộc hẹn đã được thanh toán.";
    public static final String APPOINTMENT_CANCELLED = "Cuộc hẹn đã huỷ.";
    public static final String PAYMENT_CREATE_FAILED = "Tạo thanh toán thất bại.";
    public static final String INVALID_SIGNATURE = "Invalid signature";
    public static final String INVALID_PAYMENT_AMOUNT = "Payment amount must be greater than zero";
    public static final String PAYOS_SIGNATURE_GENERATION_FAILED = "Unable to generate PayOS signature";
    public static final String INVALID_PAYOS_WEBHOOK = "Invalid PayOS webhook";
    public static final String INVALID_PAYOS_PAYMENT_REQUEST = "Invalid PayOS payment request";
    public static final String INVALID_PAYOS_ORDER_CODE = "Invalid PayOS order code";
    public static final String PAYOS_PROVIDER_ERROR = "PayOS payment provider is unavailable";
    public static final String PAYMENT_ORDER_MISMATCH = "Payment order does not match the local transaction";
    public static final String PAYOS_DESCRIPTION_PREFIX = "SH";
    public static final String PAYOS_PENDING_TRANSACTION_REQUEST_ID = "pending";
    public static final String TRANSACTION_NOT_FOUND = "Không tìm thấy giao dịch";
    public static final String APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết cuộc hẹn.";
    public static final String PAYMENT_PROCESSING_FAILED = "Lỗi xử lý thanh toán";
    public static final String PAYMENT_PROCESSING_ERROR = "Lỗi xử lí";
    public static final String PAYMENT_SUCCESS = "Thanh toán thành công";
    public static final String PAYMENT_FAILED = "Thanh toán thất bại";
    public static final String PAYMENT_AMOUNT_MISMATCH = "Số tiền thanh toán không khớp.";
    public static final String PAYMENT_PENDING = "Payment is pending confirmation";
    public static final BigDecimal DEPOSIT_PAYMENT_RATE = BigDecimal.valueOf(20, 2);

    public static final String APPOINTMENT_ID_REQUIRED = "Appointment id is required";
    public static final String APPOINTMENT_ID_POSITIVE = "Appointment id must be positive";
    public static final String PAYMENT_APPOINTMENT_FORBIDDEN = "You do not have permission to pay for this appointment";

    private PaymentMessages() {
    }
}
