package com.S_Health.GenderHealthCare.utils;

import com.S_Health.GenderHealthCare.modules.user.domain.User;

import com.S_Health.GenderHealthCare.common.security.CurrentUserProvider;
import org.springframework.stereotype.Component;

/**
 * Backward-compatible adapter for legacy services.
 */
@Component
public class AuthUtil {
    private final CurrentUserProvider currentUserProvider;

    public AuthUtil(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    public Long getCurrentUserId() {
        return currentUserProvider.requireUserId();
    }

    public User getCurrentUser() {
        return currentUserProvider.requireUser();
    }
}
