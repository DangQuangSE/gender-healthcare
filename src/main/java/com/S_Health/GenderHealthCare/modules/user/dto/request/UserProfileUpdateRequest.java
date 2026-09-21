package com.S_Health.GenderHealthCare.modules.user.dto.request;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Fields that the current user is allowed to update on their profile.
 */
@Getter
@Setter
public class UserProfileUpdateRequest {
    @NotBlank(message = UserMessages.FULLNAME_REQUIRED)
    @Size(max = 50, message = UserMessages.FULLNAME_TOO_LONG)
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = UserMessages.FULLNAME_INVALID)
    private String fullname;

    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String imageUrl;
}
