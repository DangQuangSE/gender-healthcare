package com.S_Health.GenderHealthCare.modules.identity.service;

import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence.AuthenticationRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security adapter that loads users without coupling the authentication use-case
 * service to Spring Security's AuthenticationManager construction.
 */
@Service
public class AuthenticationUserDetailsService implements UserDetailsService {
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationUserDetailsService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authenticationRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(IdentityMessages.USER_NOT_FOUND.formatted(email));
        }
        if (!user.isActive()) {
            throw new UsernameNotFoundException(IdentityMessages.ACCOUNT_INACTIVE);
        }
        return user;
    }
}
