package com.S_Health.GenderHealthCare.integrations;

/**
 * Messages used by external service integrations.
 */
public final class IntegrationMessages {
    public static final String ZOOM_TOKEN_NOT_FOUND = "Không nhận được access token từ Zoom.";
    public static final String ZOOM_TOKEN_REQUEST_FAILED = "Không thể yêu cầu access token từ Zoom.";
    public static final String ZOOM_MEETING_ALREADY_EXISTS = "Cuộc họp đã được tạo trước đó.";
    public static final String ZOOM_MEETING_LINKS_INCOMPLETE =
            "Dữ liệu cuộc họp Zoom không đầy đủ. Vui lòng liên hệ bộ phận hỗ trợ.";
    public static final String ZOOM_PAYMENT_REQUIRED =
            "Lịch hẹn cần được thanh toán thành công trước khi tạo phòng tư vấn trực tuyến.";
    public static final String ZOOM_SLOT_TIME_MISSING =
            "Lịch hẹn chưa có thời gian bắt đầu hợp lệ.";
    public static final String ZOOM_APPOINTMENT_INVALID =
            "Lịch hẹn chưa được xác nhận hoặc không phải tư vấn trực tuyến.";
    public static final String ZOOM_USER_NOT_IN_MEETING = "Bạn không phải người tham gia cuộc họp này.";
    public static final String ZOOM_MEETING_CREATE_FAILED = "Không thể tạo cuộc họp Zoom. Vui lòng thử lại sau.";
    public static final String ZOOM_MEETING_REQUEST_FAILED = "Không thể yêu cầu tạo cuộc họp Zoom.";
    public static final String ZOOM_APPOINTMENT_NOT_FOUND = "Không tìm thấy lịch hẹn.";
    public static final String ZOOM_APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết lịch hẹn.";
    public static final String STORAGE_FILE_EMPTY = "Tệp không được để trống.";
    public static final String EMAIL_SEND_FAILED = "Email sending failed: %s";
    public static final String EMAIL_PASSWORD_RESET_FAILED = "Password reset email sending failed: %s";
    public static final String EMAIL_WELCOME_FAILED = "Welcome email sending failed: %s";
    public static final String EMAIL_ZOOM_CUSTOMER_FAILED = "Zoom customer email sending failed: %s";
    public static final String EMAIL_ZOOM_CONSULTANT_FAILED = "Zoom consultant email sending failed: %s";
    public static final String EMAIL_REMINDER_SENT = "Appointment reminder email sent to: %s";
    public static final String EMAIL_REMINDER_FAILED = "Appointment reminder email sending failed: %s";
    public static final String OTP_EMAIL_MESSAGE = "You requested an account verification code. Your OTP is:";
    public static final String OTP_EMAIL_SUBJECT = "Registration verification code";
    public static final String PASSWORD_RESET_EMAIL_MESSAGE = "You requested a password reset. Your OTP is:";
    public static final String PASSWORD_RESET_EMAIL_SUBJECT = "Password reset verification code";
    public static final String WELCOME_EMAIL_SUBJECT = "Welcome to S-HealthCare";
    public static final String ACCOUNT_CREDENTIALS_EMAIL_SUBJECT = "Account information %s - S-HealthCare";
    public static final String ZOOM_EMAIL_SUBJECT = "[SHealthCare] Your consultation details";
    public static final String APPOINTMENT_REMINDER_EMAIL_SUBJECT =
            "Appointment reminder: Your upcoming appointment - SHealthCare";
    public static final String STORAGE_UPLOAD_SUCCESS = "File uploaded successfully to Cloudinary in folder: {}";
    public static final String STORAGE_UPLOAD_ERROR = "Error uploading file to Cloudinary: {}";
    public static final String STORAGE_DELETE_SUCCESS = "File deleted successfully from Cloudinary";
    public static final String STORAGE_DELETE_ERROR = "Error deleting file from Cloudinary: {}";

    private IntegrationMessages() {
    }
}
