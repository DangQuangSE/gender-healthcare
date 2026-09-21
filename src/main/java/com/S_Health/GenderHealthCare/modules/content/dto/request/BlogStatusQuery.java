package com.S_Health.GenderHealthCare.modules.content.dto.request;

import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.enums.BlogStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogStatusQuery extends BlogPageQuery {
    @NotNull(message = ContentMessages.BLOG_STATUS_REQUIRED)
    private BlogStatus status;
}
