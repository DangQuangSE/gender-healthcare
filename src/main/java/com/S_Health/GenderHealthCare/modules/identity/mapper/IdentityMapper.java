package com.S_Health.GenderHealthCare.modules.identity.mapper;

import com.S_Health.GenderHealthCare.modules.identity.dto.response.JwtResponse;
import com.S_Health.GenderHealthCare.modules.identity.dto.response.LoginResponse;
import com.S_Health.GenderHealthCare.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

/**
 * Maps the legacy authentication response to the identity module contract.
 */
@Component
public class IdentityMapper {
    private final UserMapper userMapper;

    public IdentityMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public LoginResponse toLoginResponse(JwtResponse source) {
        if (source == null) {
            return null;
        }

        return LoginResponse.builder()
                .jwt(source.getJwt())
                .user(userMapper.toResponse(source.getUser()))
                .loginProvider(source.getLoginProvider())
                .success(source.isSuccess())
                .build();
    }
}
