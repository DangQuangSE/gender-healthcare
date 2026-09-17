package com.S_Health.GenderHealthCare.modules.identity.api;

import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.authentication.EmailRegisterRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.LoginEmailRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.OAuthLoginRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.PasswordRequest;
import com.S_Health.GenderHealthCare.dto.request.authentication.VerifyOTPRequest;
import com.S_Health.GenderHealthCare.dto.response.JwtResponse;
import com.S_Health.GenderHealthCare.modules.identity.application.IdentityFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class IdentityAPI {
    private final IdentityFacade identityFacade;

    public IdentityAPI(IdentityFacade identityFacade) {
        this.identityFacade = identityFacade;
    }

    @PostMapping("/registration/otp")
    @Operation(summary = "Request registration OTP")
    public ResponseEntity<ApiResponse<String>> requestRegistrationOtp(
            @Valid @RequestBody EmailRegisterRequest request) {
        if (identityFacade.emailExists(request.getEmail())) {
            throw new ApiException(ErrorCode.CONFLICT, "Email already exists");
        }

        identityFacade.sendRegistrationOtp(request.getEmail());
        return success("Registration OTP sent");
    }

    @PostMapping("/registration/verify-otp")
    @Operation(summary = "Verify registration OTP")
    public ResponseEntity<ApiResponse<String>> verifyRegistrationOtp(
            @RequestBody VerifyOTPRequest request) {
        if (!identityFacade.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, "OTP is invalid or expired");
        }
        return success("OTP is valid");
    }

    @PostMapping("/registration/password")
    @Operation(summary = "Set registration password")
    public ResponseEntity<ApiResponse<String>> setRegistrationPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityFacade.setPassword(request);
        return success("Password configured successfully");
    }

    @PostMapping("/forgot-password/otp")
    @Operation(summary = "Request forgot-password OTP")
    public ResponseEntity<ApiResponse<String>> requestForgotPasswordOtp(
            @RequestBody EmailRegisterRequest request) {
        if (!identityFacade.emailExists(request.getEmail())) {
            throw new ApiException(ErrorCode.NOT_FOUND, "Email is not registered");
        }

        identityFacade.sendForgotPasswordOtp(request.getEmail());
        return success("Password reset OTP sent");
    }

    @PostMapping("/forgot-password/verify-otp")
    @Operation(summary = "Verify forgot-password OTP")
    public ResponseEntity<ApiResponse<String>> verifyForgotPasswordOtp(
            @RequestBody VerifyOTPRequest request) {
        if (!identityFacade.verifyOtp(request.getEmail(), request.getOtp())) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, "OTP is invalid or expired");
        }
        return success("OTP is valid");
    }

    @PutMapping("/forgot-password/password")
    @Operation(summary = "Reset password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody PasswordRequest request) {
        identityFacade.resetPassword(request);
        return success("Password reset successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginEmailRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityFacade.login(request), null));
    }

    @PostMapping("/oauth/google")
    @Operation(summary = "Login with Google")
    public ResponseEntity<ApiResponse<JwtResponse>> loginWithGoogle(
            @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityFacade.loginWithGoogle(request), null));
    }

    @PostMapping("/oauth/facebook")
    @Operation(summary = "Login with Facebook")
    public ResponseEntity<ApiResponse<JwtResponse>> loginWithFacebook(
            @RequestBody OAuthLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(identityFacade.loginWithFacebook(request), null));
    }

    private ResponseEntity<ApiResponse<String>> success(String message) {
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }
}
