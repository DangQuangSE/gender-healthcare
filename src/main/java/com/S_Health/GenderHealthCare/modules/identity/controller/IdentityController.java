package com.S_Health.GenderHealthCare.modules.identity.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.authentication.EmailRegisterRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.LoginEmailRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.PasswordRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.VerifyOTPRequest;
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
    @Operation(summary = "Request registration OTP")
    public ResponseEntity<ApiResponse<String>> requestRegistrationOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        identityService.requestRegistrationOtp(request.getEmail());
        return success("Registration OTP sent");
    }

    @PostMapping("/registration/verify-otp")
    @Operation(summary = "Verify registration OTP")
    public ResponseEntity<ApiResponse<String>> verifyRegistrationOtp(
            @Valid @RequestBody VerifyOTPRequest request) {
        identityService.verifyOtpOrThrow(request.getEmail(), request.getOtp());
        return success("OTP is valid");
    }

    @PostMapping("/registration/password")
    @Operation(summary = "Set registration password")
    public ResponseEntity<ApiResponse<String>> setRegistrationPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityService.setPassword(request);
        return success("Password configured successfully");
    }

    @PostMapping("/forgot-password/otp")
    @Operation(summary = "Request forgot-password OTP")
    public ResponseEntity<ApiResponse<String>> requestForgotPasswordOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        identityService.requestForgotPasswordOtp(request.getEmail());
        return success("Password reset OTP sent");
    }

    @PostMapping("/forgot-password/verify-otp")
    @Operation(summary = "Verify forgot-password OTP")
    public ResponseEntity<ApiResponse<String>> verifyForgotPasswordOtp(
            @Valid @RequestBody VerifyOTPRequest request) {
        identityService.verifyOtpOrThrow(request.getEmail(), request.getOtp());
        return success("OTP is valid");
    }

    @PutMapping("/forgot-password/password")
    @Operation(summary = "Reset password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityService.resetPassword(request);
        return success("Password reset successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginEmailRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.login(request), null));
    }

    @PostMapping("/oauth/google")
    @Operation(summary = "Login with Google")
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithGoogle(
            @Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.loginWithGoogle(request), null));
    }

    @PostMapping("/oauth/facebook")
    @Operation(summary = "Login with Facebook")
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithFacebook(
            @Valid @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityService.loginWithFacebook(request), null));
    }

    private ResponseEntity<ApiResponse<String>> success(String message) {
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}
