package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CertificationRequest;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationController {
    private final UserService userService;

    public CertificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = UserMessages.CREATE_CERTIFICATION)
    public ApiResponse<CertificationResponse> createCertification(
            @Valid @ModelAttribute CertificationRequest request) {
        return ApiResponse.success(userService.createCertification(request), null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = UserMessages.UPDATE_CERTIFICATION)
    public ApiResponse<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @Valid @ModelAttribute CertificationRequest request) {
        return ApiResponse.success(userService.updateCertification(id, request), null);
    }

    @GetMapping("/me")
    @Operation(summary = UserMessages.GET_CERTIFICATIONS)
    public ApiResponse<List<CertificationResponse>> getMyCertifications() {
        return ApiResponse.success(userService.getMyCertifications(), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = UserMessages.DELETE_CERTIFICATION)
    public ApiResponse<String> deleteCertification(@PathVariable Long id) {
        userService.deleteCertification(id);
        return ApiResponse.success(UserMessages.CERTIFICATION_DELETE_SUCCESS, null);
    }
}
