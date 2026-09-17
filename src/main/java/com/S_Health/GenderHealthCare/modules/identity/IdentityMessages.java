package com.S_Health.GenderHealthCare.modules.identity;

public final class IdentityMessages {
    public static final String REGISTRATION_EMAIL_EXISTS = "Email already exists";
    public static final String REGISTRATION_OTP_SENT = "Registration OTP sent";
    public static final String OTP_VALID = "OTP is valid";
    public static final String OTP_INVALID = "OTP is invalid or expired";
    public static final String PASSWORD_CONFIGURED = "Password configured successfully";
    public static final String FORGOT_PASSWORD_EMAIL_NOT_FOUND = "Email is not registered";
    public static final String FORGOT_PASSWORD_OTP_SENT = "Password reset OTP sent";
    public static final String PASSWORD_RESET_SUCCESS = "Password reset successfully";
    public static final String PASSWORD_CONFIRMATION_MISMATCH = "Passwords do not match";
    public static final String USER_NOT_FOUND = "User not found with email: %s";
    public static final String LOGIN_INVALID = "Invalid email or password";
    public static final String ACCOUNT_INACTIVE = "Account is inactive";
    public static final String GOOGLE_TOKEN_INVALID = "Invalid Google token";
    public static final String GOOGLE_LOGIN_FAILED = "Google login failed: %s";
    public static final String FACEBOOK_TOKEN_INVALID = "Invalid Facebook token";
    public static final String FACEBOOK_LOGIN_FAILED = "Facebook login failed: %s";

    private IdentityMessages() {
    }
}
