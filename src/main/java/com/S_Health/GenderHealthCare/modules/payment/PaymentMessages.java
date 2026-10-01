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
    public static final String INVALID_SIGNATURE = "Chữ ký thanh toán không hợp lệ.";
    public static final String INVALID_PAYMENT_AMOUNT = "Số tiền thanh toán phải lớn hơn 0.";
    public static final String PAYOS_SIGNATURE_GENERATION_FAILED = "Không thể tạo chữ ký PayOS.";
    public static final String INVALID_PAYOS_WEBHOOK = "Webhook PayOS không hợp lệ.";
    public static final String INVALID_PAYOS_PAYMENT_REQUEST = "Yêu cầu thanh toán PayOS không hợp lệ.";
    public static final String INVALID_PAYOS_ORDER_CODE = "Mã đơn hàng PayOS không hợp lệ.";
    public static final String PAYOS_PROVIDER_ERROR = "Dịch vụ thanh toán PayOS hiện không khả dụng.";
    public static final String PAYMENT_ORDER_MISMATCH = "Đơn thanh toán không khớp với giao dịch nội bộ.";
    public static final String PAYOS_DESCRIPTION_PREFIX = "SH";
    public static final String PAYOS_PENDING_TRANSACTION_REQUEST_ID = "pending";
    public static final String TRANSACTION_NOT_FOUND = "Không tìm thấy giao dịch";
    public static final String APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết cuộc hẹn.";
    public static final String PAYMENT_PROCESSING_FAILED = "Lỗi xử lý thanh toán";
    public static final String PAYMENT_PROCESSING_ERROR = "Lỗi xử lí";
    public static final String PAYMENT_SUCCESS = "Thanh toán thành công";
    public static final String PAYMENT_FAILED = "Thanh toán thất bại";
    public static final String PAYMENT_AMOUNT_MISMATCH = "Số tiền thanh toán không khớp.";
    public static final String PAYMENT_PENDING = "Thanh toán đang chờ xác nhận.";
    public static final BigDecimal DEPOSIT_PAYMENT_RATE = BigDecimal.valueOf(20, 2);

    public static final String APPOINTMENT_ID_REQUIRED = "Mã lịch hẹn là bắt buộc.";
    public static final String APPOINTMENT_ID_POSITIVE = "Mã lịch hẹn phải là số dương.";
    public static final String PAYMENT_APPOINTMENT_FORBIDDEN = "Bạn không có quyền thanh toán cho lịch hẹn này.";

    private PaymentMessages() {
    }
}
