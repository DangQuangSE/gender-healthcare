package com.S_Health.GenderHealthCare.integrations;

/**
 * Messages used by external service integrations.
 */
public final class IntegrationMessages {
    public static final String ZOOM_TOKEN_NOT_FOUND = "Unable to get an access token from Zoom";
    public static final String ZOOM_MEETING_ALREADY_EXISTS = "The meeting has already been created";
    public static final String ZOOM_APPOINTMENT_INVALID =
            "The appointment is not confirmed or is not an online consultation";
    public static final String ZOOM_USER_NOT_IN_MEETING = "You are not a participant in this meeting";
    public static final String ZOOM_MEETING_CREATE_FAILED = "Unable to create the Zoom meeting, please try again later";
    public static final String ZOOM_APPOINTMENT_NOT_FOUND = "Appointment not found";
    public static final String ZOOM_APPOINTMENT_DETAIL_NOT_FOUND = "Appointment detail not found";
    public static final String STORAGE_FILE_EMPTY = "File cannot be empty";
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
