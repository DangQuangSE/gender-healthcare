package com.S_Health.GenderHealthCare.modules.content.dto.request;

import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPageQuery {
    @Min(value = 0, message = ContentMessages.BLOG_PAGE_INVALID)
    private int page = 0;

    @Min(value = 1, message = ContentMessages.BLOG_SIZE_INVALID)
    @Max(value = 100, message = ContentMessages.BLOG_SIZE_INVALID)
    private int size = 10;
}
