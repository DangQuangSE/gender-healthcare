package com.S_Health.GenderHealthCare.modules.identity.dto.response;


import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class JwtResponse {
     String jwt;
     UserDetailResponse user;
     String loginProvider;
     boolean success;
}
