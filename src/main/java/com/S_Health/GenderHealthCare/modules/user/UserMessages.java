package com.S_Health.GenderHealthCare.modules.user;

public final class UserMessages {
    public static final String CERTIFICATION_NAME_REQUIRED = "Certification name is required";
    public static final String FULLNAME_REQUIRED = "Fullname is required";
    public static final String FULLNAME_TOO_LONG = "Fullname must not exceed 50 characters";
    public static final String FULLNAME_INVALID = "Fullname may contain only letters and spaces";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String PHONE_INVALID = "Phone number must be 10 digits";
    public static final String DATE_OF_BIRTH_REQUIRED = "Date of birth is required";
    public static final String DATE_OF_BIRTH_PAST = "Date of birth must be in the past";
    public static final String GENDER_REQUIRED = "Gender is required";
    public static final String ROLE_REQUIRED = "Role is required";
    public static final String SPECIALIZATIONS_REQUIRED = "Specialization ids must not be empty";
    public static final String EMAIL_EXISTS = "Email already exists in the system";
    public static final String CONSULTANT_SPECIALIZATION_REQUIRED = "A consultant must have at least one specialization";
    public static final String CUSTOMER_CREATION_FORBIDDEN = "Customers cannot be created through this API";
    public static final String SPECIALIZATION_NOT_FOUND = "One or more specializations do not exist";
    public static final String USER_NOT_CONSULTANT = "This user is not a consultant";
    public static final String SPECIALIZATION_NOT_ASSIGNED = "The consultant does not have this specialization";
    public static final String USER_NOT_FOUND = "User not found with id: %d";
    public static final String USER_ACTIVE = "The user is already active";
    public static final String PROFILE_NOT_FOUND = "User not found";
    public static final String CERTIFICATION_ROLE_REQUIRED = "Only consultants can manage certifications";
    public static final String CERTIFICATION_NOT_FOUND_OR_FORBIDDEN =
            "Certification not found or you do not have permission to manage it";
    public static final String CERTIFICATION_DELETE_SUCCESS = "Certification deleted successfully";
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
    public static final String USER_DEACTIVATED_SUCCESS = "User deactivated successfully";
    public static final String USER_RESTORED_SUCCESS = "User restored successfully";
    public static final String SPECIALIZATIONS_ADDED_SUCCESS = "Specializations added successfully";
    public static final String SPECIALIZATION_REMOVED_SUCCESS = "Specialization removed successfully";
    public static final String GET_PROFILE = "Get current user profile";
    public static final String UPDATE_PROFILE = "Update current user profile";
    public static final String UPDATE_AVATAR = "Update current user avatar";
    public static final String AVATAR_REQUIRED = "Avatar file is required";
    public static final String PROFILE_UPDATE_LEGACY = "Update personal information";
    public static final String PROFILE_GET_LEGACY = "Get personal information";

    private UserMessages() {
    }
}
