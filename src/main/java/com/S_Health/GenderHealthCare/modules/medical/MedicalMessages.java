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
    public static final String PROFILE_UPDATE_FAILED = "Không thể cập nhật hồ sơ y tế từ kết quả";

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
    public static final String MEDICAL_INFO_UPDATED = "Cập nhật thông tin y tế thành công.";
    public static final String GET_MEDICAL_INFO = "Get detailed medical information";
    public static final String CREATE_CONSULTATION_RESULT = "Create a consultation result";
    public static final String CREATE_LAB_RESULT = "Create a lab test result";
    public static final String CREATE_PROTOCOL = "Create a treatment protocol";
    public static final String SERVICE_ID_REQUIRED = "Mã dịch vụ là bắt buộc.";
    public static final String CUSTOMER_ID_REQUIRED = "Mã khách hàng là bắt buộc.";
    public static final String PAGE_NOT_NEGATIVE = "Số trang không được âm.";
    public static final String PAGE_SIZE_INVALID = "Kích thước trang phải lớn hơn 0.";
    public static final String APPOINTMENT_DETAIL_ID_REQUIRED = "Mã chi tiết lịch hẹn là bắt buộc.";
    public static final String APPOINTMENT_DETAIL_ID_POSITIVE = "Mã chi tiết lịch hẹn phải là số dương.";
    public static final String DESCRIPTION_REQUIRED = "Mô tả kết quả là bắt buộc.";
    public static final String DESCRIPTION_TOO_SHORT = "Mô tả kết quả phải có ít nhất 10 ký tự.";
    public static final String DIAGNOSIS_REQUIRED = "Chẩn đoán là bắt buộc.";
    public static final String DIAGNOSIS_TOO_SHORT = "Chẩn đoán phải có ít nhất 10 ký tự.";
    public static final String TREATMENT_PLAN_REQUIRED = "Phác đồ điều trị là bắt buộc.";
    public static final String TREATMENT_PLAN_TOO_SHORT = "Phác đồ điều trị phải có ít nhất 10 ký tự.";
    public static final String RESULT_TYPE_REQUIRED = "Loại kết quả là bắt buộc.";
    public static final String TEST_NAME_REQUIRED = "Tên xét nghiệm là bắt buộc.";
    public static final String TEST_RESULT_REQUIRED = "Kết quả xét nghiệm là bắt buộc.";
    public static final String TEST_STATUS_REQUIRED = "Trạng thái xét nghiệm là bắt buộc.";
    public static final String PATIENT_ID_REQUIRED = "Mã bệnh nhân là bắt buộc.";
    public static final String MEDICAL_INFO_UPDATED_SUCCESS = "Cập nhật thông tin y tế thành công.";
    public static final String PROTOCOL_DELETED = "Xóa phác đồ điều trị thành công.";
    public static final String CONSULTANT_UNASSIGNED = "Chưa phân công";
    public static final String DIAGNOSIS_UNAVAILABLE = "Chưa có thông tin chẩn đoán";
    public static final String ONLINE_CONSULTATION = "Tư vấn trực tuyến";
    public static final String ROOM_UNASSIGNED = "Chưa phân phòng";
    public static final String MEDICAL_RESULT_TAG = "Medical Result API";
    public static final String MEDICAL_RESULT_TAG_DESCRIPTION = "API for managing consultation and lab results";

    public static final String TREATMENT_PROTOCOL_ACCESS_FORBIDDEN = "Bạn không có quyền xem phác đồ điều trị này.";
    public static final String RESULT_ACCESS_FORBIDDEN = "Bạn không có quyền xem kết quả y tế này.";
    public static final String RESULT_WRITE_FORBIDDEN = "Bạn không có quyền ghi kết quả y tế này.";
    public static final String RESULT_DELETED = "Xóa kết quả y tế thành công.";
    public static final String TREATMENT_PROTOCOL_ID_REQUIRED = "Mã phác đồ điều trị là bắt buộc.";
    public static final String TREATMENT_PROTOCOL_ID_POSITIVE = "Mã phác đồ điều trị phải là số dương.";

    private MedicalMessages() {
    }
}
