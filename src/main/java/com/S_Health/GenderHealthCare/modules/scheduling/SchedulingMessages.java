package com.S_Health.GenderHealthCare.modules.scheduling;

/**
 * Messages used by scheduling endpoints.
 */
public final class SchedulingMessages {
    public static final String GET_CONSULTANT_SCHEDULE = "Get a consultant schedule";
    public static final String GET_AVAILABLE_SLOTS = "Get available slots for a service";
    public static final String REGISTER_SCHEDULE = "Register a consultant schedule";
    public static final String CANCEL_SCHEDULE = "Cancel a consultant schedule";
    public static final String GET_WORKING_DOCTORS = "Get doctors working on a date";

    public static final String SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ!";
    public static final String CONSULTANT_NOT_FOUND = "Không tìm thấy người tư vấn này!";
    public static final String WORK_DATE_MUST_BE_FUTURE = "%sNgày làm việc phải sau ngày hiện tại";
    public static final String DUPLICATE_WORK_DATE = "%s bị trùng trong danh sách";
    public static final String FULL_DAY_CANCEL_CANNOT_HAVE_SLOT = "Không được truyền slot khi huỷ nguyên ngày.";
    public static final String PARTIAL_DAY_CANCEL_REQUIRES_SLOT = "Phải truyền slot khi không huỷ nguyên ngày.";
    public static final String SLOTS_NOT_FOUND = "Không tìm thấy slots cần huỷ.";
    public static final String SLOT_NOT_FOUND = "Không tìm thấy slot cần huỷ.";
    public static final String SCHEDULE_CANCELLED = "Đã huỷ lịch thành công";
    public static final String APPOINTMENT_CANCELLED = "Đã huỷ";
    public static final String MAX_BOOKING_CONFIG_KEY = "MAX_BOOKING";
    public static final int DEFAULT_MAX_BOOKING = 6;
    public static final long SLOT_DURATION_MINUTES = 90;
    public static final String DATE_REQUIRED = "Date is required";

    private SchedulingMessages() {
    }
}
