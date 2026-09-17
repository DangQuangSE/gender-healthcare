package com.S_Health.GenderHealthCare.modules.user.dto.request;

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
    @NotBlank(message = "Fullname is required")
    private String fullname;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String address;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Role is required")
    private UserRole role;

    private String imageUrl;

    // Chỉ yêu cầu cho CONSULTANT
    private Set<Long> specializationIds;

}
