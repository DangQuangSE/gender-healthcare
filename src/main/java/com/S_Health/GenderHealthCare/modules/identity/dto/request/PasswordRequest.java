package com.S_Health.GenderHealthCare.modules.identity.dto.request;

import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordRequest {
    @Email(message = IdentityMessages.EMAIL_INVALID)
    @NotBlank(message = IdentityMessages.EMAIL_REQUIRED)
    String email;

    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = IdentityMessages.PASSWORD_INVALID)
    @NotBlank(message = IdentityMessages.PASSWORD_REQUIRED)
    String password;

    @NotBlank(message = IdentityMessages.PASSWORD_CONFIRM_REQUIRED)
    String confirmPassword;
}
