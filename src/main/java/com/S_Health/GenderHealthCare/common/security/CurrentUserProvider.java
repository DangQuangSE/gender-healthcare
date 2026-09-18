package com.S_Health.GenderHealthCare.common.security;

import com.S_Health.GenderHealthCare.modules.user.domain.User;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public User requireUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new DomainException(ErrorCode.UNAUTHENTICATED);
        }
        return user;
    }

    public Long requireUserId() {
        return requireUser().getId();
    }
}
