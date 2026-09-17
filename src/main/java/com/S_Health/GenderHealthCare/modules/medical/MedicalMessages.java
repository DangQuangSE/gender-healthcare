package com.S_Health.GenderHealthCare.modules.medical;

/**
 * Messages used by the medical module.
 */
public final class MedicalMessages {
    public static final String WRITER_NOT_FOUND = "Không thể tìm thấy người nhập!";
    public static final String APPOINTMENT_DETAIL_NOT_FOUND = "Không tìm thấy chi tiết cuộc hẹn!";
    public static final String TREATMENT_PROTOCOL_NOT_FOUND = "Không tìm thấy phác đồ!";
    public static final String RESULT_NOT_FOUND_OR_DELETED = "Không tìm thấy kết quả hoặc đã bị xóa!";
    public static final String RESULT_UPDATE_NOT_FOUND = "Không tìm thấy kết quả để cập nhật!";
    public static final String RESULT_DELETE_NOT_FOUND = "Không tìm thấy kết quả để xóa!";
    public static final String PROFILE_UPDATE_FAILED = "Lỗi khi update Medical Profile từ Result: %s";

    public static final String SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ này!";
    public static final String MEDICAL_PROFILE_NOT_FOUND = "Không tìm thấy hồ sơ khám bệnh!";
    public static final String HISTORY_ACCESS_FORBIDDEN = "Chỉ bác sĩ mới có thể xem lịch sử khám bệnh";
    public static final String PATIENT_NOT_FOUND = "Không tìm thấy bệnh nhân";
    public static final String PROFILE_ACCESS_FORBIDDEN = "Bạn không có quyền xem hồ sơ bệnh nhân này";
    public static final String MEDICAL_INFO_UPDATE_FORBIDDEN = "Chỉ staff mới có thể cập nhật thông tin y tế";

    public static final String PROTOCOL_ID_NOT_FOUND = "Không tìm thấy ID";
    public static final String GET_MY_PROFILE = "Get my medical profile";
    public static final String GET_PATIENT_HISTORY = "Get a patient's medical history";
    public static final String UPDATE_MEDICAL_INFO = "Update basic medical information";
    public static final String MEDICAL_INFO_UPDATED = "Medical information updated";
    public static final String GET_MEDICAL_INFO = "Get detailed medical information";
    public static final String CREATE_CONSULTATION_RESULT = "Create a consultation result";
    public static final String CREATE_LAB_RESULT = "Create a lab test result";
    public static final String CREATE_PROTOCOL = "Create a treatment protocol";
    public static final String SERVICE_ID_REQUIRED = "Service id is required";
    public static final String CUSTOMER_ID_REQUIRED = "Customer id is required";
    public static final String PAGE_NOT_NEGATIVE = "Page must not be negative";
    public static final String PAGE_SIZE_INVALID = "Size must be greater than zero";
    public static final String APPOINTMENT_DETAIL_ID_REQUIRED = "Appointment detail id is required";
    public static final String APPOINTMENT_DETAIL_ID_POSITIVE = "Appointment detail id must be positive";
    public static final String DESCRIPTION_REQUIRED = "Result description is required";
    public static final String DESCRIPTION_TOO_SHORT = "Result description must contain at least 10 characters";
    public static final String DIAGNOSIS_REQUIRED = "Diagnosis is required";
    public static final String DIAGNOSIS_TOO_SHORT = "Diagnosis must contain at least 10 characters";
    public static final String TREATMENT_PLAN_REQUIRED = "Treatment plan is required";
    public static final String TREATMENT_PLAN_TOO_SHORT = "Treatment plan must contain at least 10 characters";
    public static final String RESULT_TYPE_REQUIRED = "Result type is required";
    public static final String TEST_NAME_REQUIRED = "Test name is required";
    public static final String TEST_RESULT_REQUIRED = "Test result is required";
    public static final String TEST_STATUS_REQUIRED = "Test status is required";
    public static final String PATIENT_ID_REQUIRED = "Patient id is required";
    public static final String MEDICAL_INFO_UPDATED_SUCCESS = "Medical information updated successfully";
    public static final String PROTOCOL_DELETED = "Treatment protocol deleted successfully";
    public static final String CONSULTANT_UNASSIGNED = "Not assigned";
    public static final String DIAGNOSIS_UNAVAILABLE = "No diagnosis available";
    public static final String ONLINE_CONSULTATION = "Online consultation";
    public static final String ROOM_UNASSIGNED = "Room not assigned";
    public static final String MEDICAL_RESULT_TAG = "Medical Result API";
    public static final String MEDICAL_RESULT_TAG_DESCRIPTION = "API for managing consultation and lab results";

    private MedicalMessages() {
    }
}
