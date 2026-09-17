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

    private MedicalMessages() {
    }
}
