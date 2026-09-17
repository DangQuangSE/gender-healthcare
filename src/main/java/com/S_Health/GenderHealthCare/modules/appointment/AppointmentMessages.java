package com.S_Health.GenderHealthCare.modules.appointment;

/**
 * Messages used by the appointment and booking module.
 */
public final class AppointmentMessages {
    public static final String APPOINTMENT_NOT_FOUND = "Không tìm thấy lịch hẹn!";
    public static final String RESULT_NOT_FOUND = "Không có kết quả!";
    public static final String UPDATE_FORBIDDEN = "Bạn không có quyền chỉnh sửa lịch hẹn này!";
    public static final String SLOT_NOT_FOUND = "Không tìm thấy slot mới";
    public static final String SLOT_CHANGE_TOO_LATE = "Chỉ được đổi slot trước ít nhất 1 ngày!";
    public static final String CONSULTANT_NOT_FOUND = "Không tìm thấy tư vấn viên!";
    public static final String ALREADY_CANCELED = "Lịch hẹn đã bị hủy trước đó.";
    public static final String MEDICAL_PROFILE_NOT_FOUND = "Lịch hẹn này không có hồ sơ y tế";
    public static final String APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết cuộc hẹn";
    public static final String DETAIL_UPDATE_FORBIDDEN = "Bạn chỉ có thể cập nhật dịch vụ mà bạn phụ trách";
    public static final String DETAIL_STATUS_INVALID =
            "Chỉ có thể cập nhật trạng thái IN_PROGRESS, WAITING_RESULT hoặc COMPLETED";

    public static final String BOOKING_SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ!";
    public static final String BOOKING_SLOT_NOT_FOUND = "Không tìm thấy khung giờ này!";
    public static final String BOOKING_SLOT_MISMATCH = "Khung giờ không khớp với ngày/giờ yêu cầu!";
    public static final String CUSTOMER_NOT_FOUND = "Không tìm thấy khách hàng!";
    public static final String DUPLICATE_BOOKING = "Bạn đã có lịch hẹn vào khung giờ này";
    public static final String CONSULTANT_SERVICE_MISMATCH = "Bác sĩ được chọn không phù hợp với dịch vụ này!";
    public static final String CONSULTANT_SLOT_UNAVAILABLE = "Bác sĩ không có lịch trống vào thời gian này!";
    public static final String CONSULTANT_SLOT_FULL = "Bác sĩ đã hết slot trong khung giờ này!";
    public static final String CONSULTANT_SLOT_NOT_FOUND = "Khung giờ không tồn tại";
    public static final String NO_AVAILABLE_CONSULTANT =
            "Không tìm thấy tư vấn viên nào khả dụng cho thời gian đã chọn!";
    public static final String AUTO_ASSIGN_ROOM_FAILED = "Có lỗi xảy ra khi auto-assign phòng: %s";
    public static final String CONSULTANT_CHECK_FAILED = "Lỗi khi kiểm tra tư vấn viên %s: %s";
    public static final String STATUS_UPDATE_FAILED = "Lỗi khi cập nhật trạng thái: %s";
    public static final String APPOINTMENT_STATUS_UPDATE_FAILED =
            "Không thể cập nhật trạng thái lịch hẹn: %s";

    public static final String GET_APPOINTMENT = "Get appointment by ID";
    public static final String GET_CONSULTANT_SCHEDULE = "Get the current consultant schedule";
    public static final String GET_BY_STATUS = "Get appointments by status";
    public static final String UPDATE_APPOINTMENT = "Update an appointment";
    public static final String DELETE_APPOINTMENT = "Delete an appointment";
    public static final String CANCEL_APPOINTMENT = "Cancel an appointment";
    public static final String CHECK_IN_APPOINTMENT = "Check in an appointment";
    public static final String GET_PATIENT_HISTORY = "Get patient history from an appointment";
    public static final String UPDATE_DETAIL_STATUS = "Update an appointment detail status";
    public static final String DETAIL_STATUS_UPDATED = "Appointment detail status updated";
    public static final String RATE_APPOINTMENT = "Mark an appointment as rated";
    public static final String CREATE_BOOKING = "Create an appointment booking";
    public static final String APPOINTMENT_DATE_REQUIRED = "Appointment date is required";

    private AppointmentMessages() {
    }
}
