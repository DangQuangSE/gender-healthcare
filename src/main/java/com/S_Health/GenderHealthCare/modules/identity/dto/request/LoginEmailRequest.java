package com.S_Health.GenderHealthCare.modules.identity.dto.request;

import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginEmailRequest {
    @Email(message = IdentityMessages.EMAIL_INVALID)
    @NotBlank(message = IdentityMessages.EMAIL_REQUIRED)
    String email;
    @NotBlank(message = IdentityMessages.PASSWORD_REQUIRED)
    String password;
}
