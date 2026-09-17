package com.S_Health.GenderHealthCare.common.message;

/**
 * Messages shared by more than one backend module.
 */
public final class CommonMessages {
    public static final String REQUEST_SUCCESS_CODE = "SUCCESS";
    public static final String REQUEST_COMPLETED = "Request completed successfully";

    public static final String IMAGE_REQUIRED = "Image file is required";
    public static final String IMAGE_TOO_LARGE = "Image file must not exceed 5 MB";
    public static final String IMAGE_ONLY = "Only image files are allowed";
    public static final String IMAGE_NAME_INVALID = "Invalid image file name";
    public static final String FILE_EMPTY = "File cannot be empty";
    public static final String IMAGE_UPLOAD_FAILED = "Unable to upload image: %s";

    public static final String CORS_WILDCARD_WITH_CREDENTIALS =
            "CORS_ALLOWED_ORIGINS cannot contain * when credentials are enabled";
    public static final String LOG_BAD_REQUEST = "Bad request at {}: {}";
    public static final String LOG_DATA_CONFLICT = "Data conflict at {}";
    public static final String LOG_UNEXPECTED_ERROR = "Unexpected error at {}";
    public static final String LOG_REQUEST =
            "request method={} path={} status={} durationMs={} requestId={}";

    private CommonMessages() {
    }
}
