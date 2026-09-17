package com.S_Health.GenderHealthCare.modules.identity.dto.response;

import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String jwt;
    private UserResponse user;
    private String loginProvider;
    private boolean success;
}
