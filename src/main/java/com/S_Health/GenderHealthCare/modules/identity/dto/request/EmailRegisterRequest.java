package com.S_Health.GenderHealthCare.modules.identity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailRegisterRequest {
   @Email(message = "Email không hợp lệ!")
    @NotBlank(message = "Email khong duoc de trong")
    String email;
}
