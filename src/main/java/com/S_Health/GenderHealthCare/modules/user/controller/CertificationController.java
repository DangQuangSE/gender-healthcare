package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.user.service.UserService;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationController {
    private final UserService userService;

    public CertificationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create consultant certification")
    public ApiResponse<CertificationResponse> createCertification(
            @NotBlank @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("image") MultipartFile image) {
        return ApiResponse.success(userService.createCertification(name, image), null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update consultant certification")
    public ApiResponse<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @NotBlank @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return ApiResponse.success(userService.updateCertification(id, name, image), null);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current consultant certifications")
    public ApiResponse<List<CertificationResponse>> getMyCertifications() {
        return ApiResponse.success(userService.getMyCertifications(), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete consultant certification")
    public ApiResponse<String> deleteCertification(@PathVariable Long id) {
        userService.deleteCertification(id);
        return ApiResponse.success("Certification deleted successfully", null);
    }
}
