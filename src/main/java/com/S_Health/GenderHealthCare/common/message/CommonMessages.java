package com.S_Health.GenderHealthCare.common.message;

/**
 * Messages shared by more than one backend module.
 */
public final class CommonMessages {
    public static final String REQUEST_SUCCESS_CODE = "SUCCESS";
    public static final String REQUEST_COMPLETED = "Request completed successfully";

    public static final String ERROR_BAD_REQUEST = "Request is invalid";
    public static final String ERROR_VALIDATION_FAILED = "Request validation failed";
    public static final String ERROR_AUTHENTICATION_REQUIRED = "Authentication is required";
    public static final String ERROR_ACCESS_DENIED = "You do not have permission to perform this action";
    public static final String ERROR_RESOURCE_NOT_FOUND = "The requested resource was not found";
    public static final String ERROR_RESOURCE_CONFLICT = "The request conflicts with the current resource state";
    public static final String ERROR_INTEGRATION_FAILED = "External service request failed";
    public static final String ERROR_INTERNAL = "An unexpected server error occurred";
    public static final String ERROR_METHOD_NOT_ALLOWED = "The HTTP method is not supported for this resource";
    public static final String ERROR_MEDIA_TYPE_NOT_SUPPORTED = "The request media type is not supported";
    public static final String ERROR_PAYLOAD_TOO_LARGE = "The request payload is too large";

    public static final String IMAGE_REQUIRED = "Image file is required";
    public static final String IMAGE_TOO_LARGE = "Image file must not exceed 5 MB";
    public static final String IMAGE_ONLY = "Only image files are allowed";
    public static final String IMAGE_NAME_INVALID = "Invalid image file name";
    public static final String FILE_EMPTY = "File cannot be empty";
    public static final String IMAGE_UPLOAD_FAILED = "Unable to upload image";

    public static final String CORS_WILDCARD_WITH_CREDENTIALS =
            "CORS_ALLOWED_ORIGINS cannot contain * when credentials are enabled";
    public static final String LOG_BAD_REQUEST = "Bad request at {}: {}";
    public static final String LOG_DATA_CONFLICT = "Data conflict at {}";
    public static final String LOG_DOMAIN_ERROR = "Domain error at {}";
    public static final String LOG_UNEXPECTED_ERROR = "Unexpected error at {}";
    public static final String LOG_INTEGRATION_ERROR = "External integration failed at {}";
    public static final String LOG_REQUEST =
            "request method={} path={} status={} durationMs={} requestId={}";

    private CommonMessages() {
    }
}
