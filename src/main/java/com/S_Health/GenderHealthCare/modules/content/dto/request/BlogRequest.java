package com.S_Health.GenderHealthCare.modules.content.dto.request;

import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.enums.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogRequest {
    @NotBlank(message = ContentMessages.BLOG_TITLE_REQUIRED)
    String title;

    @NotBlank(message = ContentMessages.BLOG_CONTENT_REQUIRED)
    String content;

    BlogStatus status;
    MultipartFile image;
    String imgUrl;
    List<String> tagNames;

}
