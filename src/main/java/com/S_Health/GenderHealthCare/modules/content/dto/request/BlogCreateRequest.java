package com.S_Health.GenderHealthCare.modules.content.dto.request;

import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * Required fields for creating a blog post.
 */
public class BlogCreateRequest extends BlogRequest {
    @NotNull(message = ContentMessages.BLOG_IMAGE_REQUIRED)
    private MultipartFile image;

    @Override
    public MultipartFile getImage() {
        return image;
    }

    @Override
    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
