package com.S_Health.GenderHealthCare.modules.content.controller;

import com.S_Health.GenderHealthCare.modules.content.dto.request.CommentRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.response.CommentResponse;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@SecurityRequirement(name = "api")
public class LegacyCommentsController {
    private final CommentService commentService;

    public LegacyCommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getCommentsByBlog(blogId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ContentMessages.DELETE_COMMENT_SUCCESS);
    }
}
