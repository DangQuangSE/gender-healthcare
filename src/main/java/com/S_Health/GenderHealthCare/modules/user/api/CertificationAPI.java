package com.S_Health.GenderHealthCare.modules.user.api;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.response.certification.CertificationResponse;
import com.S_Health.GenderHealthCare.modules.user.application.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationAPI {
    private final UserFacade userFacade;

    public CertificationAPI(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create consultant certification")
    public ApiResponse<CertificationResponse> createCertification(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("image") MultipartFile image) {
        return ApiResponse.success(userFacade.createCertification(name, image), null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update consultant certification")
    public ApiResponse<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        return ApiResponse.success(userFacade.updateCertification(id, name, image), null);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current consultant certifications")
    public ApiResponse<List<CertificationResponse>> getMyCertifications() {
        return ApiResponse.success(userFacade.getMyCertifications(), null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete consultant certification")
    public ApiResponse<String> deleteCertification(@PathVariable Long id) {
        userFacade.deleteCertification(id);
        return ApiResponse.success("Certification deleted successfully", null);
    }
}
