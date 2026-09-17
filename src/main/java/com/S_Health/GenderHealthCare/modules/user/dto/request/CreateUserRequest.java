package com.S_Health.GenderHealthCare.modules.user.dto.request;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.user.enums.Gender;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class CreateUserRequest {
    @NotBlank(message = UserMessages.FULLNAME_REQUIRED)
    private String fullname;

    @Email(message = UserMessages.EMAIL_INVALID)
    @NotBlank(message = UserMessages.EMAIL_REQUIRED)
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = UserMessages.PHONE_INVALID)
    private String phone;

    @NotNull(message = UserMessages.DATE_OF_BIRTH_REQUIRED)
    @Past(message = UserMessages.DATE_OF_BIRTH_PAST)
    private LocalDate dateOfBirth;

    private String address;

    @NotNull(message = UserMessages.GENDER_REQUIRED)
    private Gender gender;

    @NotNull(message = UserMessages.ROLE_REQUIRED)
    private UserRole role;

    private String imageUrl;

    // Chỉ yêu cầu cho CONSULTANT
    private Set<Long> specializationIds;

}
