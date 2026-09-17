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
    public static final String GOOGLE_LOGIN_FAILED = "Google login failed";
    public static final String FACEBOOK_TOKEN_INVALID = "Invalid Facebook token";
    public static final String FACEBOOK_LOGIN_FAILED = "Facebook login failed";
    public static final String EMAIL_INVALID = "Email is invalid";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String OTP_REQUIRED = "OTP is required";
    public static final String ACCESS_TOKEN_REQUIRED = "Access token is required";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_INVALID = "Password must contain at least 8 characters, including a letter and a number";
    public static final String PASSWORD_CONFIRM_REQUIRED = "Please confirm the password";
    public static final String REQUEST_REGISTRATION_OTP = "Request registration OTP";
    public static final String VERIFY_REGISTRATION_OTP = "Verify registration OTP";
    public static final String SET_REGISTRATION_PASSWORD = "Set registration password";
    public static final String REQUEST_FORGOT_PASSWORD_OTP = "Request forgot-password OTP";
    public static final String VERIFY_FORGOT_PASSWORD_OTP = "Verify forgot-password OTP";
    public static final String RESET_PASSWORD = "Reset password";
    public static final String LOGIN_EMAIL = "Login with email and password";
    public static final String LOGIN_GOOGLE = "Login with Google";
    public static final String LOGIN_FACEBOOK = "Login with Facebook";

    private IdentityMessages() {
    }
}
