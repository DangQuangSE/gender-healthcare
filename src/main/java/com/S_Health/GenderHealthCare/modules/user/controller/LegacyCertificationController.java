package com.S_Health.GenderHealthCare.modules.user.controller;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import com.S_Health.GenderHealthCare.modules.user.dto.response.CertificationResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.request.CertificationRequest;
import com.S_Health.GenderHealthCare.modules.user.service.CertificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certifications")
@SecurityRequirement(name = "api")
/**
 * Legacy compatibility controller. Use modules.user.controller.CertificationController for /api/v1.
 */
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyCertificationController {
    private final CertificationService certificationService;

    public LegacyCertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificationResponse> createCertification(
            @Valid @ModelAttribute CertificationRequest request) {

        CertificationResponse response = certificationService.createCertification(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable Long id,
            @Valid @ModelAttribute CertificationRequest request) {

        CertificationResponse response = certificationService.updateCertification(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-certifications")
    public ResponseEntity<List<CertificationResponse>> getMyCertifications() {
        List<CertificationResponse> certifications = certificationService.getMyCertifications();
        return ResponseEntity.ok(certifications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertification(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.ok(UserMessages.CERTIFICATION_DELETE_SUCCESS);
    }
}
