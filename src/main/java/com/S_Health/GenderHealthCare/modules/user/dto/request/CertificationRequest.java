package com.S_Health.GenderHealthCare.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Multipart request used to create or update a consultant certification.
 */
@Getter
@Setter
public class CertificationRequest {
    @NotBlank(message = UserMessages.CERTIFICATION_NAME_REQUIRED)
    private String name;

    private String description;
    private MultipartFile image;
}
