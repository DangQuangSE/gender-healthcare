package com.S_Health.GenderHealthCare.modules.catalog;

public final class CatalogConstants {
    public static final String CONSULTANT_ROLE = "CONSULTANT";

    public static final String GET_SERVICES = "Get active services with optional filters";
    public static final String GET_SERVICE = "Get service by ID";
    public static final String CREATE_SERVICE = "Create service";
    public static final String UPDATE_SERVICE = "Update service";
    public static final String ACTIVATE_SERVICE = "Activate service";
    public static final String DEACTIVATE_SERVICE = "Deactivate service";
    public static final String CREATE_COMBO_SERVICE = "Create combo service";
    public static final String GET_SPECIALIZATIONS = "Get active specializations with optional search";
    public static final String GET_SPECIALIZATION = "Get specialization by ID";
    public static final String CREATE_SPECIALIZATION = "Create specialization";
    public static final String UPDATE_SPECIALIZATION = "Update specialization";
    public static final String DELETE_SPECIALIZATION = "Delete specialization";
    public static final String GET_ROOMS = "Get active rooms with optional specialization filter";
    public static final String GET_ROOM = "Get room by ID";
    public static final String CREATE_ROOM = "Create room";
    public static final String UPDATE_ROOM = "Update room";
    public static final String DELETE_ROOM = "Delete room";
    public static final String ASSIGN_CONSULTANT = "Assign consultant to room";
    public static final String REMOVE_CONSULTANT = "Remove consultant from room";
    public static final String GET_ROOM_CONSULTANTS = "Get room consultants";
    public static final String GET_TAGS = "Get tags";
    public static final String GET_TAG = "Get tag by ID";
    public static final String CREATE_TAG = "Create tag";
    public static final String UPDATE_TAG = "Update tag";
    public static final String DELETE_TAG = "Delete tag";
    public static final String GET_CONFIGS = "Get configurations";
    public static final String CREATE_CONFIG = "Create configuration";
    public static final String UPDATE_CONFIG = "Update configuration";
    public static final String DELETE_CONFIG = "Delete configuration";
    public static final String GET_CONSULTANTS = "Get consultants with optional service filter";

    public static final String SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ với ID: %d";
    public static final String SERVICE_INACTIVE = "Dịch vụ không hoạt động";
    public static final String SERVICE_INACTIVE_UPDATE = "Dịch vụ không hoạt động, không thể cập nhật";
    public static final String SERVICE_NAME_EXISTS = "Tên dịch vụ đã tồn tại";
    public static final String COMBO_NAME_EXISTS = "Tên dịch vụ combo đã tồn tại";
    public static final String COMBO_REQUIRES_ITEMS = "Dịch vụ combo phải có ít nhất 1 dịch vụ thành phần";
    public static final String SPECIALIZATION_NOT_FOUND = "Không tìm thấy chuyên môn với ID: %d";
    public static final String SPECIALIZATION_INACTIVE = "Chuyên môn không hoạt động";
    public static final String SPECIALIZATION_INACTIVE_ID = "Chuyên môn với ID: %d không hoạt động";
    public static final String SPECIALIZATION_NAME_EXISTS = "Tên chuyên môn đã tồn tại";
    public static final String SPECIALIZATION_DELETED_UPDATE = "Không thể cập nhật chuyên môn đã bị xóa";
    public static final String SPECIALIZATION_ALREADY_DELETED = "Chuyên môn đã bị xóa trước đó";
    public static final String SPECIALIZATION_IN_USE_BY_SERVICES = "Không thể xóa chuyên môn đang được sử dụng bởi các dịch vụ";
    public static final String SPECIALIZATION_IN_USE_BY_CONSULTANTS = "Không thể xóa chuyên môn đang được sử dụng bởi các bác sĩ";
    public static final String SUB_SERVICE_NOT_FOUND = "Không tìm thấy dịch vụ thành phần với ID: %d";
    public static final String SUB_SERVICE_INACTIVE = "Dịch vụ thành phần với ID: %d không hoạt động";
    public static final String ROOM_NAME_EXISTS = "Phòng với tên này đã tồn tại";
    public static final String ROOM_NOT_FOUND = "Không tìm thấy phòng";
    public static final String ROOM_INACTIVE = "Phòng không còn hoạt động";
    public static final String ROOM_ALREADY_DELETED = "Phòng đã bị xóa trước đó";
    public static final String CONSULTANT_NOT_FOUND = "Không tìm thấy bác sĩ";
    public static final String USER_NOT_CONSULTANT = "Người dùng không phải là bác sĩ";
    public static final String CONSULTANT_SPECIALIZATION_MISMATCH = "Bác sĩ không có chuyên môn phù hợp với phòng này";
    public static final String WORKING_TIME_INVALID = "Giờ bắt đầu phải trước giờ kết thúc";
    public static final String WORKING_TIME_EXISTS = "Lịch làm việc này đã tồn tại";
    public static final String WORKING_TIME_NOT_FOUND = "Không tìm thấy lịch làm việc";
    public static final String WORKING_TIME_WRONG_ROOM = "Lịch làm việc không thuộc phòng này";
    public static final String WORKING_TIME_ALREADY_DELETED = "Lịch làm việc đã bị xóa trước đó";
    public static final String TAG_EXISTS = "Tag đã tồn tại";
    public static final String TAG_NOT_FOUND = "Không tìm thấy tag";
    public static final String CONFIG_EXISTS = "Cấu hình đã tồn tại";
    public static final String CONFIG_NOT_FOUND = "Không tìm thấy cấu hình";

    public static final String SPECIALIZATION_DELETED = "Specialization deleted successfully";
    public static final String ROOM_DELETED = "Room deleted successfully";
    public static final String CONSULTANT_REMOVED_FROM_ROOM = "Consultant removed from room";
    public static final String TAG_DELETED = "Tag deleted successfully";
    public static final String CONFIGURATION_DELETED = "Configuration deleted successfully";

    private CatalogConstants() {
    }
}
