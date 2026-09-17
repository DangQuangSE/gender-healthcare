package com.S_Health.GenderHealthCare.common.validation;

import com.S_Health.GenderHealthCare.common.exception.ApiException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validates image uploads before they reach storage integrations.
 */
@Component
public class ImageUploadValidator {
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;

    public void validateRequired(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Image file is required");
        }
        validate(file);
    }

    public void validateOptional(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            validate(file);
        }
    }

    private void validate(MultipartFile file) {
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Image file must not exceed 5 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Only image files are allowed");
        }

        String filename = file.getOriginalFilename();
        if (filename != null && (filename.contains("..")
                || filename.contains("/")
                || filename.contains("\\"))) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "Invalid image file name");
        }
    }
}
