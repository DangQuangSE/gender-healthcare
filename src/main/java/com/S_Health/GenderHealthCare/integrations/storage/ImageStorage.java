package com.S_Health.GenderHealthCare.integrations.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Storage boundary used by business modules for image files.
 */
public interface ImageStorage {
    String uploadImage(MultipartFile file) throws IOException;

    String uploadCertificationImage(MultipartFile file) throws IOException;

    Map<?, ?> deleteImage(String publicId) throws IOException;
}
