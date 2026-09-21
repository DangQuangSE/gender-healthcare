package com.S_Health.GenderHealthCare.modules.content.controller;

import com.S_Health.GenderHealthCare.modules.content.enums.BlogStatus;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;

import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogCreateRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.response.BlogResponse;
import com.S_Health.GenderHealthCare.modules.content.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog")
@SecurityRequirement(name = "api")
public class LegacyBlogController {
    private final BlogService blogService;

    public LegacyBlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{id}")
    @Operation(summary = ContentMessages.VIEW_BLOG)
    public ResponseEntity getBlogAndIncreaseView(@PathVariable long id) {
        return ResponseEntity.ok(blogService.viewBlog(id));
    }

    @PostMapping("/{id}/like")
    @Operation(summary = ContentMessages.LIKE_BLOG)
    public ResponseEntity<String> likeBlog(@PathVariable long id) {
        blogService.likeBlog(id);
        return ResponseEntity.ok(ContentMessages.LIKE_BLOG_SUCCESS);
    }

    @GetMapping("/summary")
    @Operation(summary = ContentMessages.GET_BLOG_SUMMARY)
    public ResponseEntity getSummaryBlog() {
        return ResponseEntity.ok(blogService.getAllBlogSummaries());
    }

    @GetMapping
    @Operation(summary = ContentMessages.GET_PUBLISHED_BLOGS)
    public ResponseEntity<Page<BlogResponse>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getAllBlogs(page, size));
    }

    @GetMapping("/admin/all")
    @Operation(summary = ContentMessages.GET_BLOGS_FOR_MANAGEMENT)
    public ResponseEntity<Page<BlogResponse>> getAllBlogsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getAllBlogsForManagement(page, size));
    }

    @GetMapping("/by-tag/{tagId}")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_TAG)
    public ResponseEntity<Page<BlogResponse>> getBlogsByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getBlogsByTag(tagId, page, size));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.CREATE_BLOG)
    public ResponseEntity createBlogWithImage(@Valid @ModelAttribute BlogCreateRequest request) {
        // Không cần set status nữa, sẽ được xử lý tự động trong service
        return ResponseEntity.ok(blogService.createBlog(request));
    }

    // API mới cho tác giả
    @GetMapping("/my-blogs")
    @Operation(summary = ContentMessages.GET_MY_BLOGS)
    public ResponseEntity<Page<BlogResponse>> getMyBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getMyBlogs(page, size));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = ContentMessages.GET_BLOG_DETAIL)
    public ResponseEntity<BlogResponse> getBlogDetail(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.UPDATE_BLOG)
    public ResponseEntity updateBlog(
            @PathVariable Long id,
            @Valid @ModelAttribute BlogRequest request) {
        return ResponseEntity.ok(blogService.updateBlog(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = ContentMessages.DELETE_BLOG)
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(ContentMessages.DELETE_BLOG_SUCCESS);
    }

    // API cho admin xem blog theo status
    @GetMapping("/admin/by-status")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_STATUS)
    public ResponseEntity<Page<BlogResponse>> getBlogsByStatus(
            @RequestParam BlogStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getBlogsByStatus(status, page, size));
    }

    // API admin duyệt blog
    @PostMapping("/admin/{id}/approve")
    @Operation(summary = ContentMessages.APPROVE_BLOG)
    public ResponseEntity<BlogResponse> approveBlog(
            @PathVariable Long id) {
        return ResponseEntity.ok(blogService.approveBlog(id));
    }

    // API admin từ chối blog
    @PostMapping("/admin/{id}/reject")
    @Operation(summary = ContentMessages.REJECT_BLOG)
    public ResponseEntity<BlogResponse> rejectBlog(
            @PathVariable Long id) {
        return ResponseEntity.ok(blogService.rejectBlog(id));
    }

    // API admin đăng blog
    @PostMapping("/admin/{id}/publish")
    @Operation(summary = ContentMessages.PUBLISH_BLOG)
    public ResponseEntity<BlogResponse> publishBlog(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.publishBlog(id));
    }

    // API author gửi blog để duyệt
    @PostMapping("/{id}/submit")
    @Operation(summary = ContentMessages.SUBMIT_BLOG)
    public ResponseEntity<BlogResponse> submitBlogForReview(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.submitBlogForReview(id));
    }

    // API author xem blog của mình theo status
    @GetMapping("/my-blogs/by-status")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_STATUS)
    public ResponseEntity<Page<BlogResponse>> getMyBlogsByStatus(
            @RequestParam BlogStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(blogService.getMyBlogsByStatus(status, page, size));
    }
}
