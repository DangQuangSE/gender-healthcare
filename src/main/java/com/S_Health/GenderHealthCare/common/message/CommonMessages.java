package com.S_Health.GenderHealthCare.common.message;

/**
 * Messages shared by more than one backend module.
 */
public final class CommonMessages {
    public static final String IMAGE_REQUIRED = "Tệp hình ảnh là bắt buộc.";
    public static final String IMAGE_TOO_LARGE = "Tệp hình ảnh không được vượt quá 5 MB.";
    public static final String IMAGE_ONLY = "Chỉ chấp nhận tệp hình ảnh.";
    public static final String IMAGE_NAME_INVALID = "Tên tệp hình ảnh không hợp lệ.";
    public static final String FILE_EMPTY = "Tệp không được để trống.";
    public static final String IMAGE_UPLOAD_FAILED = "Không thể tải hình ảnh lên.";

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
