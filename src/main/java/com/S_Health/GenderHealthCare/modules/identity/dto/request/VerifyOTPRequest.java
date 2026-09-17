package com.S_Health.GenderHealthCare.modules.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VerifyOTPRequest {
     @Email(message = "Email khong hop le")
     @NotBlank(message = "Email khong duoc de trong")
     String email;
     @NotBlank(message = "OTP is required")
     String otp;
}
