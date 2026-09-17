package com.S_Health.GenderHealthCare.integrations.storage;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.integrations.IntegrationMessages;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService implements ImageStorage {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadImage(file, "blog_images");
    }

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException(CommonMessages.FILE_EMPTY);
            }

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"
                    )
            );
            log.info(IntegrationMessages.STORAGE_UPLOAD_SUCCESS, folder);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            log.error(IntegrationMessages.STORAGE_UPLOAD_ERROR, e.getMessage(), e);
            throw e;
        }
    }

    public String uploadCertificationImage(MultipartFile file) throws IOException {
        return uploadImage(file, "certifications");
    }
    public Map<?, ?> deleteImage(String publicId) throws IOException {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info(IntegrationMessages.STORAGE_DELETE_SUCCESS);
            return result;
        } catch (IOException e) {
            log.error(IntegrationMessages.STORAGE_DELETE_ERROR, e.getMessage(), e);
            throw e;
        }
    }
}
