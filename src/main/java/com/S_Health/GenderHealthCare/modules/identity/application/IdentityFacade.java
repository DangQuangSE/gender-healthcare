package com.S_Health.GenderHealthCare.modules.identity.application;

import com.S_Health.GenderHealthCare.dto.request.authentication.LoginEmailRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.PasswordRequest;
import com.S_Health.GenderHealthCare.dto.response.JwtResponse;
import com.S_Health.GenderHealthCare.service.authentication.AuthenticationService;
import com.S_Health.GenderHealthCare.service.authentication.OTPService;
import org.springframework.stereotype.Service;

/**
 * Application boundary for identity use cases.
 *
 * The business implementation is still legacy for now. Keeping the dependency
 * here allows the new API to migrate without duplicating authentication logic.
 */
@Service
public class IdentityFacade {
    private final AuthenticationService authenticationService;
    private final OTPService otpService;

    public IdentityFacade(
            AuthenticationService authenticationService,
            OTPService otpService) {
        this.authenticationService = authenticationService;
        this.otpService = otpService;
    }

    public boolean emailExists(String email) {
        return authenticationService.checkExistEmail(email);
    }

    public void sendRegistrationOtp(String email) {
        otpService.generateOTP(email);
    }

    public boolean verifyOtp(String email, String otp) {
        return otpService.verifyOtp(email, otp);
    }

    public void setPassword(PasswordRequest request) {
        authenticationService.setPassword(request);
    }

    public void sendForgotPasswordOtp(String email) {
        otpService.generateForgotPasswordOTP(email);
    }

    public void resetPassword(PasswordRequest request) {
        authenticationService.setPasswordForgot(request);
    }

    public JwtResponse login(LoginEmailRequest request) {
        return authenticationService.loginWithEmail(request);
    }

    public JwtResponse loginWithGoogle(OAuthLoginRequest request) {
        return authenticationService.loginWithGoogleToken(request.getAccessToken());
    }

    public JwtResponse loginWithFacebook(OAuthLoginRequest request) {
        return authenticationService.loginWithFacebook(request.getAccessToken());
    }
}
