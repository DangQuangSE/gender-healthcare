package com.S_Health.GenderHealthCare.modules.identity.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.identity.IdentityMessages;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.EmailRegisterRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.LoginEmailRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.PasswordRequest;
import com.S_Health.GenderHealthCare.modules.identity.dto.request.VerifyOTPRequest;
import com.S_Health.GenderHealthCare.modules.identity.service.IdentityService;
import com.S_Health.GenderHealthCare.modules.identity.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class IdentityController {
    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping("/registration/otp")
    @Operation(summary = IdentityMessages.REQUEST_REGISTRATION_OTP)
    public ResponseEntity<ApiResponse<String>> requestRegistrationOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        identityService.requestRegistrationOtp(request.getEmail());
        return success(IdentityMessages.REGISTRATION_OTP_SENT);
    }

    @PostMapping("/registration/verify-otp")
    @Operation(summary = IdentityMessages.VERIFY_REGISTRATION_OTP)
    public ResponseEntity<ApiResponse<String>> verifyRegistrationOtp(
            @Valid @RequestBody VerifyOTPRequest request) {
        identityService.verifyOtpOrThrow(request.getEmail(), request.getOtp());
        return success(IdentityMessages.OTP_VALID);
    }

    @PostMapping("/registration/password")
    @Operation(summary = IdentityMessages.SET_REGISTRATION_PASSWORD)
    public ResponseEntity<ApiResponse<String>> setRegistrationPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityService.setPassword(request);
        return success(IdentityMessages.PASSWORD_CONFIGURED);
    }

    @PostMapping("/forgot-password/otp")
    @Operation(summary = IdentityMessages.REQUEST_FORGOT_PASSWORD_OTP)
    public ResponseEntity<ApiResponse<String>> requestForgotPasswordOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        identityService.requestForgotPasswordOtp(request.getEmail());
        return success(IdentityMessages.FORGOT_PASSWORD_OTP_SENT);
    }

    @PostMapping("/forgot-password/verify-otp")
    @Operation(summary = IdentityMessages.VERIFY_FORGOT_PASSWORD_OTP)
    public ResponseEntity<ApiResponse<String>> verifyForgotPasswordOtp(
            @Valid @RequestBody VerifyOTPRequest request) {
        identityService.verifyOtpOrThrow(request.getEmail(), request.getOtp());
        return success(IdentityMessages.OTP_VALID);
    }

    @PutMapping("/forgot-password/password")
    @Operation(summary = IdentityMessages.RESET_PASSWORD)
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityService.resetPassword(request);
        return success(IdentityMessages.PASSWORD_RESET_SUCCESS);
    }

    @PostMapping("/login")
    @Operation(summary = IdentityMessages.LOGIN_EMAIL)
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginEmailRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.login(request), null));
    }

    @PostMapping("/oauth/google")
    @Operation(summary = IdentityMessages.LOGIN_GOOGLE)
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithGoogle(
            @Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.loginWithGoogle(request), null));
    }

    @PostMapping("/oauth/facebook")
    @Operation(summary = IdentityMessages.LOGIN_FACEBOOK)
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithFacebook(
            @Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.loginWithFacebook(request), null));
    }

    private ResponseEntity<ApiResponse<String>> success(String message) {
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}
