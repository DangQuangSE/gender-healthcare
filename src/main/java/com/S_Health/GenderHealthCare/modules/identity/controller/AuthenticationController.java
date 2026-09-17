package com.S_Health.GenderHealthCare.modules.identity.controller;

import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.EmailRegisterRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.LoginEmailRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.PasswordRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.VerifyOTPRequest;
import com.S_Health.GenderHealthCare.modules.identity.service.AuthenticationService;
import com.S_Health.GenderHealthCare.modules.identity.service.OTPService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Legacy endpoint compatibility. New clients should use IdentityController
 * under /api/v1/auth.
 */
@Deprecated(since = "1.0", forRemoval = false)
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final OTPService otpService;

    public AuthenticationController(
            AuthenticationService authenticationService,
            OTPService otpService) {
        this.authenticationService = authenticationService;
        this.otpService = otpService;
    }

    @PostMapping("/auth/request-OTP")
    public ResponseEntity<String> requestRegistrationOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        if (authenticationService.checkExistEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(IdentityMessages.REGISTRATION_EMAIL_EXISTS);
        }
        otpService.generateOTP(request.getEmail());
        return ResponseEntity.ok(IdentityMessages.REGISTRATION_OTP_SENT);
    }

    @PostMapping("/auth/verify-Otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOTPRequest request) {
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        return valid
                ? ResponseEntity.ok(IdentityMessages.OTP_VALID)
                : ResponseEntity.badRequest().body(IdentityMessages.OTP_INVALID);
    }

    @PostMapping("/auth/config-password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody PasswordRequest request) {
        authenticationService.setPassword(request);
        return ResponseEntity.ok(IdentityMessages.PASSWORD_CONFIGURED);
    }

    @PostMapping("/auth/forgot-password/request-otp")
    public ResponseEntity<String> requestForgotPasswordOtp(
            @RequestBody EmailRegisterRequest request) {
        if (!authenticationService.checkExistEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(IdentityMessages.FORGOT_PASSWORD_EMAIL_NOT_FOUND);
        }
        otpService.generateForgotPasswordOTP(request.getEmail());
        return ResponseEntity.ok(IdentityMessages.FORGOT_PASSWORD_OTP_SENT);
    }

    @PostMapping("/auth/forgot-password/verify-otp")
    public ResponseEntity<String> verifyForgotPasswordOtp(@RequestBody VerifyOTPRequest request) {
        boolean valid = otpService.verifyOtp(request.getEmail(), request.getOtp());
        return valid
                ? ResponseEntity.ok(IdentityMessages.OTP_VALID)
                : ResponseEntity.badRequest().body(IdentityMessages.OTP_INVALID);
    }

    @PutMapping("/auth/forgot-password/resetPass")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordRequest request) {
        authenticationService.setPasswordForgot(request);
        return ResponseEntity.ok(IdentityMessages.PASSWORD_RESET_SUCCESS);
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(authenticationService.loginWithGoogleToken(request.getAccessToken()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginWithEmail(@RequestBody LoginEmailRequest request) {
        return ResponseEntity.ok(authenticationService.loginWithEmail(request));
    }

    @PostMapping("/auth/facebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(authenticationService.loginWithFacebook(request.getAccessToken()));
    }
}
