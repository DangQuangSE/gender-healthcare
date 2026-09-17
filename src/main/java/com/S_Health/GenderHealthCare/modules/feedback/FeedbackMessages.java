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

    private FeedbackMessages() {
    }
}
