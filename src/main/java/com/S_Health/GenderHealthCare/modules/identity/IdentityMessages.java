package com.S_Health.GenderHealthCare.modules.identity;

public final class IdentityMessages {
    public static final String REGISTRATION_EMAIL_EXISTS = "Email đã được đăng ký.";
    public static final String REGISTRATION_OTP_SENT = "Mã OTP đăng ký đã được gửi.";
    public static final String OTP_VALID = "Mã OTP hợp lệ.";
    public static final String OTP_INVALID = "Mã OTP không hợp lệ hoặc đã hết hạn.";
    public static final String PASSWORD_CONFIGURED = "Thiết lập mật khẩu thành công.";
    public static final String FORGOT_PASSWORD_EMAIL_NOT_FOUND = "Email chưa được đăng ký.";
    public static final String FORGOT_PASSWORD_OTP_SENT = "Mã OTP đặt lại mật khẩu đã được gửi.";
    public static final String PASSWORD_RESET_SUCCESS = "Đặt lại mật khẩu thành công.";
    public static final String PASSWORD_CONFIRMATION_MISMATCH = "Mật khẩu xác nhận không khớp.";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng với email: %s";
    public static final String LOGIN_INVALID = "Email hoặc mật khẩu không chính xác.";
    public static final String ACCOUNT_INACTIVE = "Tài khoản đã bị vô hiệu hóa.";
    public static final String EMAIL_INVALID = "Email không hợp lệ.";
    public static final String EMAIL_REQUIRED = "Email là bắt buộc.";
    public static final String OTP_REQUIRED = "Mã OTP là bắt buộc.";
    public static final String PASSWORD_REQUIRED = "Mật khẩu là bắt buộc.";
    public static final String PASSWORD_INVALID = "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ cái và chữ số.";
    public static final String PASSWORD_CONFIRM_REQUIRED = "Vui lòng xác nhận mật khẩu.";
    public static final String REQUEST_REGISTRATION_OTP = "Request registration OTP";
    public static final String VERIFY_REGISTRATION_OTP = "Verify registration OTP";
    public static final String SET_REGISTRATION_PASSWORD = "Set registration password";
    public static final String REQUEST_FORGOT_PASSWORD_OTP = "Request forgot-password OTP";
    public static final String VERIFY_FORGOT_PASSWORD_OTP = "Verify forgot-password OTP";
    public static final String RESET_PASSWORD = "Reset password";
    public static final String LOGIN_EMAIL = "Login with email and password";

    private IdentityMessages() {
    }
}
