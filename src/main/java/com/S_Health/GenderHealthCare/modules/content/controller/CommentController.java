package com.S_Health.GenderHealthCare.modules.content.controller;

import com.S_Health.GenderHealthCare.modules.content.dto.request.CommentRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.response.CommentResponse;
import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@SecurityRequirement(name = "api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(summary = ContentMessages.CREATE_COMMENT)
    public CommentResponse createComment(@Valid @RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping("/blogs/{blogId}")
    @Operation(summary = ContentMessages.GET_COMMENTS)
    public List<CommentResponse> getCommentsByBlog(@PathVariable Long blogId) {
        return commentService.getCommentsByBlog(blogId);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = ContentMessages.DELETE_COMMENT)
    public ApiResponse<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success(ContentMessages.DELETE_COMMENT_SUCCESS, null);
    }
}
