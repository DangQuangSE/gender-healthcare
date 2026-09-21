package com.S_Health.GenderHealthCare.modules.content.dto.request;

import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlogTagQuery extends BlogPageQuery {
    @NotEmpty(message = ContentMessages.BLOG_TAGS_REQUIRED_QUERY)
    private List<@Positive(message = ContentMessages.BLOG_TAG_ID_INVALID) Long> tags;
}
