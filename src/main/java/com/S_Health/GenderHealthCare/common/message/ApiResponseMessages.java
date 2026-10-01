package com.S_Health.GenderHealthCare.common.message;

/**
 * Stable messages and codes shared by the versioned HTTP API response.
 *
 * <p>These messages are safe to expose to clients. Internal exception details
 * must not be used as an API response message.</p>
 */
public final class ApiResponseMessages {
    public static final String SUCCESS_CODE = "SUCCESS";
    public static final String SUCCESS_MESSAGE = "Thành công.";

    public static final String BAD_REQUEST = "Yêu cầu không hợp lệ.";
    public static final String VALIDATION_FAILED = "Dữ liệu gửi lên không hợp lệ.";
    public static final String AUTHENTICATION_REQUIRED = "Vui lòng đăng nhập để tiếp tục.";
    public static final String ACCESS_DENIED = "Bạn không có quyền thực hiện thao tác này.";
    public static final String RESOURCE_NOT_FOUND = "Không tìm thấy tài nguyên yêu cầu.";
    public static final String RESOURCE_CONFLICT = "Yêu cầu xung đột với trạng thái hiện tại của tài nguyên.";
    public static final String INTEGRATION_FAILED = "Dịch vụ bên ngoài hiện không khả dụng.";
    public static final String INTERNAL_ERROR = "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.";
    public static final String METHOD_NOT_ALLOWED = "Phương thức HTTP không được hỗ trợ cho tài nguyên này.";
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "Định dạng dữ liệu gửi lên không được hỗ trợ.";
    public static final String PAYLOAD_TOO_LARGE = "Dữ liệu gửi lên vượt quá dung lượng cho phép.";

    private ApiResponseMessages() {
    }
}
