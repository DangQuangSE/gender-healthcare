package com.S_Health.GenderHealthCare.modules.identity.service;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.integrations.mail.EmailService;
import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.S_Health.GenderHealthCare.modules.identity.client.GoogleAuthClient;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.LoginEmailRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.PasswordRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.response.JwtResponse;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDTO;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.repository.AuthenticationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Application service for password, OTP and social-login authentication flows.
 */
@Service
public class AuthenticationService implements UserDetailsService {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final OTPService otpService;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final GoogleAuthClient googleAuthClient;

    public AuthenticationService(
            AuthenticationRepository authenticationRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            OTPService otpService,
            JWTService jwtService,
            ModelMapper modelMapper,
            EmailService emailService,
            GoogleAuthClient googleAuthClient) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.googleAuthClient = googleAuthClient;
    }

    public boolean checkExistEmail(String email) {
        return authenticationRepository.existsByEmail(email);
    }

    public void setPassword(PasswordRequest request) {
        validatePasswordConfirmation(request);

        authenticationRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerify(true)
                .isActive(true)
                .role(UserRole.CUSTOMER)
                .build());

        otpService.removeOtp(request.getEmail());
        emailService.sendWelcome(request.getEmail());
    }

    public void setPasswordForgot(PasswordRequest request) {
        validatePasswordConfirmation(request);

        User user = authenticationRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DomainException(
                        ErrorCode.NOT_FOUND,
                        IdentityMessages.USER_NOT_FOUND.formatted(request.getEmail())));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        authenticationRepository.save(user);
        otpService.removeOtp(request.getEmail());
    }

    public JwtResponse loginWithEmail(LoginEmailRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));
        } catch (Exception exception) {
            throw new DomainException(ErrorCode.BAD_REQUEST, IdentityMessages.LOGIN_INVALID);
        }

        User user = authenticationRepository.findUserByEmail(request.getEmail());
        ensureActive(user);
        return createJwtResponse(user, "email");
    }

    public JwtResponse loginWithGoogleToken(String googleToken) {
        GoogleAuthClient.GoogleUser googleUser = googleAuthClient.verify(googleToken);
        return loginWithSocialAccount(
                googleUser.email(),
                googleUser.name(),
                googleUser.imageUrl(),
                "google");
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

    private JwtResponse loginWithSocialAccount(
            String email,
            String fullName,
            String imageUrl,
            String provider) {
        User user = authenticationRepository.findByEmail(email).orElseGet(() ->
                authenticationRepository.save(User.builder()
                        .email(email)
                        .fullname(fullName)
                        .imageUrl(imageUrl)
                        .isVerify(true)
                        .isActive(true)
                        .role(UserRole.CUSTOMER)
                        .build()));

        ensureActive(user);
        return createJwtResponse(user, provider);
    }

    private JwtResponse createJwtResponse(User user, String provider) {
        String jwt = jwtService.generateToken(user);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new JwtResponse(jwt, userDTO, provider, true);
    }

    private void validatePasswordConfirmation(PasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new DomainException(
                    ErrorCode.BAD_REQUEST,
                    IdentityMessages.PASSWORD_CONFIRMATION_MISMATCH);
        }
    }

    private void ensureActive(User user) {
        if (user == null || !user.isActive()) {
            throw new DomainException(ErrorCode.UNAUTHENTICATED, IdentityMessages.ACCOUNT_INACTIVE);
        }
    }
}
