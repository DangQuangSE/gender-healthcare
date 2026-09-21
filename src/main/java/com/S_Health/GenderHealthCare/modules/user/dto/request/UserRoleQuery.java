package com.S_Health.GenderHealthCare.modules.user.dto.request;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleQuery {
    @NotBlank(message = UserMessages.ROLE_REQUIRED)
    private String role;
}
