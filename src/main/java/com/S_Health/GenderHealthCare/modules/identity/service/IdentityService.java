package com.S_Health.GenderHealthCare.modules.identity.service;



import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.LoginEmailRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.PasswordRequest;
import com.S_Health.GenderHealthCare.modules.identity.IdentityException;
import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.S_Health.GenderHealthCare.modules.identity.dto.response.LoginResponse;
import com.S_Health.GenderHealthCare.modules.identity.mapper.IdentityMapper;
import com.S_Health.GenderHealthCare.modules.identity.service.AuthenticationService;
import com.S_Health.GenderHealthCare.modules.identity.service.OTPService;
import org.springframework.stereotype.Service;

/**
 * Application boundary for identity use cases.
 *
 * The business implementation is still legacy for now. Keeping the dependency
 * here allows the new API to migrate without duplicating authentication logic.
 */
@Service
public class IdentityService {
    private final AuthenticationService authenticationService;
    private final OTPService otpService;
    private final IdentityMapper identityMapper;

    public IdentityService(
            AuthenticationService authenticationService,
            OTPService otpService,
            IdentityMapper identityMapper) {
        this.authenticationService = authenticationService;
        this.otpService = otpService;
        this.identityMapper = identityMapper;
    }

    public void requestRegistrationOtp(String email) {
        if (authenticationService.checkExistEmail(email)) {
            throw new IdentityException(ErrorCode.CONFLICT, IdentityMessages.REGISTRATION_EMAIL_EXISTS);
        }
        otpService.generateOTP(email);
    }

    public void verifyOtpOrThrow(String email, String otp) {
        if (!otpService.verifyOtp(email, otp)) {
            throw new IdentityException(ErrorCode.VALIDATION_ERROR, IdentityMessages.OTP_INVALID);
        }
    }

    public void setPassword(PasswordRequest request) {
        authenticationService.setPassword(request);
    }

    public void requestForgotPasswordOtp(String email) {
        if (!authenticationService.checkExistEmail(email)) {
            throw new IdentityException(
                    ErrorCode.NOT_FOUND,
                    IdentityMessages.FORGOT_PASSWORD_EMAIL_NOT_FOUND);
        }
        otpService.generateForgotPasswordOTP(email);
    }

    public void resetPassword(PasswordRequest request) {
        authenticationService.setPasswordForgot(request);
    }

    public LoginResponse login(LoginEmailRequest request) {
        return identityMapper.toLoginResponse(authenticationService.loginWithEmail(request));
    }

    public LoginResponse loginWithGoogle(OAuthLoginRequest request) {
        return identityMapper.toLoginResponse(
                authenticationService.loginWithGoogleToken(request.getAccessToken()));
    }

    public LoginResponse loginWithFacebook(OAuthLoginRequest request) {
        return identityMapper.toLoginResponse(
                authenticationService.loginWithFacebook(request.getAccessToken()));
    }
}
