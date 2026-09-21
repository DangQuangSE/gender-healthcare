package com.S_Health.GenderHealthCare.modules.identity.dto.request;

import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OAuthLoginRequest {
     @NotBlank(message = IdentityMessages.ACCESS_TOKEN_REQUIRED)
     String accessToken;
}
