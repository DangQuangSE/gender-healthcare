package com.S_Health.GenderHealthCare.modules.feedback;

/**
 * User-facing messages and API descriptions for feedback features.
 */
public final class FeedbackMessages {
    public static final String APPOINTMENT_NOT_FOUND = "Cuộc hẹn không tồn tại";
    public static final String CUSTOMER_INVALID = "Thông tin khách hàng không hợp lệ";
    public static final String APPOINTMENT_NOT_OWNED = "Bạn chưa có cuộc hẹn nào";
    public static final String FEEDBACK_NOT_FOUND = "Không tìm thấy feedback này";
    public static final String FEEDBACK_NOT_FOUND_FOR_APPOINTMENT = "Chưa có đánh giá cho cuộc hẹn này";
    public static final String FEEDBACK_UPDATE_FORBIDDEN = "Bạn không có quyền sửa đánh giá này";
    public static final String CONSULTANT_FEEDBACK_NOT_FOUND = "Không tìm thấy đánh giá bác sĩ";
    public static final String CONSULTANT_NOT_FOUND = "Không có bác sĩ này";
    public static final String CONSULTANT_NOT_IN_APPOINTMENT = "Bác sĩ không thuộc cuộc hẹn này";
    public static final String NO_CONSULTANT_FEEDBACK = "Không có đánh giá bác sĩ nào";

    public static final String FEEDBACK_ACCESS_FORBIDDEN = "You do not have permission to view this feedback";
    public static final String CONSULTANT_ROLE_REQUIRED = "Only consultants can view their consultant feedback";
    public static final String STAFF_ROLE_REQUIRED = "Only staff or admin users can view all feedback";
    public static final String CREATE_SERVICE_FEEDBACK = "Create service feedback";
    public static final String GET_SERVICE_FEEDBACK = "Get service feedback";
    public static final String UPDATE_SERVICE_FEEDBACK = "Update service feedback";
    public static final String CREATE_CONSULTANT_FEEDBACK = "Create consultant feedback";
    public static final String UPDATE_CONSULTANT_FEEDBACK = "Update consultant feedback";
    public static final String GET_MY_CONSULTANT_FEEDBACK = "Get my consultant feedback";
    public static final String GET_FEEDBACK_BY_APPOINTMENT = "Get feedback for an appointment";
    public static final String GET_FEEDBACK_BY_SERVICE = "Get feedback for a service";
    public static final String GET_AVERAGE_RATING = "Get the average service rating";
    public static final String GET_ALL_FEEDBACK = "Get all service feedback";
    public static final String GET_FEEDBACK_BY_SERVICE_FEEDBACK = "Get consultant feedback for a service feedback";
    public static final String RATING_INVALID = "Rating must be between 1 and 5";
    public static final String COMMENT_TOO_LONG = "Comment must not exceed 1000 characters";
    public static final String CONSULTANT_COMMENT_TOO_LONG = "Consultant comment must not exceed 1000 characters";
    public static final String APPOINTMENT_ID_REQUIRED = "Appointment id is required";
    public static final String APPOINTMENT_ID_POSITIVE = "Appointment id must be positive";
    public static final String CONSULTANT_FEEDBACK_TAG = "Consultant Feedback API";
    public static final String CONSULTANT_FEEDBACK_TAG_DESCRIPTION = "API for managing consultant feedback";

    private FeedbackMessages() {
    }
}
