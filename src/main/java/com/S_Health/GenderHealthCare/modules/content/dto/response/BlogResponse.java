package com.S_Health.GenderHealthCare.modules.content.dto.response;

import com.S_Health.GenderHealthCare.modules.content.enums.BlogStatus;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.TagDetailResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;
    private String imgUrl;
    private int viewCount;
    private int likeCount;
    private BlogStatus status;
    private UserDetailResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagDetailResponse> tags;
}
