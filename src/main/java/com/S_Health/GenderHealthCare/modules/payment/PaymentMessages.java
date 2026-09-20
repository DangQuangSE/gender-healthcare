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
    public static final String TRANSACTION_NOT_FOUND = "Không tìm thấy giao dịch";
    public static final String APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết cuộc hẹn.";
    public static final String PAYMENT_PROCESSING_FAILED = "Lỗi xử lý thanh toán";
    public static final String PAYMENT_PROCESSING_ERROR = "Lỗi xử lí";
    public static final String PAYMENT_SUCCESS = "Thanh toán thành công";
    public static final String PAYMENT_FAILED = "Thanh toán thất bại";
    public static final String PAYMENT_AMOUNT_MISMATCH = "Số tiền thanh toán không khớp.";
    public static final String INVALID_VNPAY_SIGNATURE = "Chữ ký không hợp lệ.";
    public static final String ORDER_INFO_FORMAT = "Thanh toan don hang: %s";
    public static final String VNPAY_VERSION = "2.1.0";
    public static final String VNPAY_COMMAND = "pay";
    public static final String VNPAY_CURRENCY = "VND";
    public static final String VNPAY_LOCALE = "vn";
    public static final String VNPAY_ORDER_TYPE = "other";
    public static final String LOCAL_IP_ADDRESS = "127.0.0.1";
    public static final String VNPAY_SIGNATURE_GENERATION_FAILED = "Unable to generate VNPay signature";
    public static final String VNPAY_TIME_ZONE = "Etc/GMT+7";
    public static final int VNPAY_TIMEOUT_MINUTES = 5;
    public static final int OFFLINE_PAYMENT_TIMEOUT_MINUTES = 1;
    public static final BigDecimal OFFLINE_PAYMENT_RATE = BigDecimal.valueOf(20, 2);

    public static final String CREATE_VNPAY_PAYMENT = "Create a VNPay payment";
    public static final String CREATE_OFFLINE_PAYMENT = "Create an offline payment order";
    public static final String HANDLE_VNPAY_RETURN = "Handle a VNPay return callback";
    public static final String APPOINTMENT_ID_REQUIRED = "Appointment id is required";
    public static final String APPOINTMENT_ID_POSITIVE = "Appointment id must be positive";
    public static final String PAYMENT_APPOINTMENT_FORBIDDEN = "You do not have permission to pay for this appointment";

    private PaymentMessages() {
    }
}
