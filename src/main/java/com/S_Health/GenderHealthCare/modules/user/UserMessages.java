package com.S_Health.GenderHealthCare.modules.user;

public final class UserMessages {
    public static final String CERTIFICATION_NAME_REQUIRED = "Tên chứng chỉ là bắt buộc.";
    public static final String FULLNAME_REQUIRED = "Họ và tên là bắt buộc.";
    public static final String FULLNAME_TOO_LONG = "Họ và tên không được vượt quá 50 ký tự.";
    public static final String FULLNAME_INVALID = "Họ và tên chỉ được chứa chữ cái và khoảng trắng.";
    public static final String EMAIL_INVALID = "Định dạng email không hợp lệ.";
    public static final String EMAIL_REQUIRED = "Email là bắt buộc.";
    public static final String PHONE_INVALID = "Số điện thoại phải có 10 chữ số.";
    public static final String DATE_OF_BIRTH_REQUIRED = "Ngày sinh là bắt buộc.";
    public static final String DATE_OF_BIRTH_PAST = "Ngày sinh phải nằm trong quá khứ.";
    public static final String GENDER_REQUIRED = "Giới tính là bắt buộc.";
    public static final String ROLE_REQUIRED = "Vai trò là bắt buộc.";
    public static final String SPECIALIZATIONS_REQUIRED = "Danh sách mã chuyên môn không được để trống.";
    public static final String EMAIL_EXISTS = "Email đã tồn tại trong hệ thống.";
    public static final String CONSULTANT_SPECIALIZATION_REQUIRED = "Tư vấn viên phải có ít nhất một chuyên môn.";
    public static final String CUSTOMER_CREATION_FORBIDDEN = "Không thể tạo tài khoản khách hàng qua API này.";
    public static final String SPECIALIZATION_NOT_FOUND = "Một hoặc nhiều chuyên môn không tồn tại.";
    public static final String USER_NOT_CONSULTANT = "Người dùng này không phải tư vấn viên.";
    public static final String SPECIALIZATION_NOT_ASSIGNED = "Tư vấn viên chưa được gán chuyên môn này.";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng với mã: %d";
    public static final String USER_ACTIVE = "Người dùng đã được kích hoạt.";
    public static final String PROFILE_NOT_FOUND = "Không tìm thấy người dùng.";
    public static final String CERTIFICATION_ROLE_REQUIRED = "Chỉ tư vấn viên mới có thể quản lý chứng chỉ.";
    public static final String CERTIFICATION_NOT_FOUND_OR_FORBIDDEN =
            "Không tìm thấy chứng chỉ hoặc bạn không có quyền quản lý chứng chỉ này.";
    public static final String CERTIFICATION_DELETE_SUCCESS = "Xóa chứng chỉ thành công.";
    public static final String CREATE_CERTIFICATION = "Create consultant certification";
    public static final String UPDATE_CERTIFICATION = "Update consultant certification";
    public static final String GET_CERTIFICATIONS = "Get current consultant certifications";
    public static final String DELETE_CERTIFICATION = "Delete consultant certification";
    public static final String CREATE_STAFF_ACCOUNT = "Create staff or consultant account";
    public static final String ADD_SPECIALIZATIONS = "Add consultant specializations";
    public static final String REMOVE_SPECIALIZATION = "Remove consultant specialization";
    public static final String GET_SPECIALIZATIONS = "Get consultant specializations";
    public static final String GET_USERS_BY_ROLE = "Get users by role";
    public static final String DEACTIVATE_USER = "Deactivate user";
    public static final String RESTORE_USER = "Restore user";
    public static final String USER_DEACTIVATED_SUCCESS = "Vô hiệu hóa người dùng thành công.";
    public static final String USER_RESTORED_SUCCESS = "Khôi phục người dùng thành công.";
    public static final String SPECIALIZATIONS_ADDED_SUCCESS = "Thêm chuyên môn thành công.";
    public static final String SPECIALIZATION_REMOVED_SUCCESS = "Xóa chuyên môn thành công.";
    public static final String GET_PROFILE = "Get current user profile";
    public static final String UPDATE_PROFILE = "Update current user profile";
    public static final String UPDATE_AVATAR = "Update current user avatar";
    public static final String AVATAR_REQUIRED = "Tệp ảnh đại diện là bắt buộc.";
    public static final String PROFILE_UPDATE_LEGACY = "Update personal information";
    public static final String PROFILE_GET_LEGACY = "Get personal information";

    private UserMessages() {
    }
}
