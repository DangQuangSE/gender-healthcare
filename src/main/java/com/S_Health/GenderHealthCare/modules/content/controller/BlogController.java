package com.S_Health.GenderHealthCare.modules.content.controller;

import com.S_Health.GenderHealthCare.dto.request.blog.BlogRequest;
import com.S_Health.GenderHealthCare.dto.response.BlogResponse;
import com.S_Health.GenderHealthCare.dto.response.BlogSummaryDTO;
import com.S_Health.GenderHealthCare.enums.BlogStatus;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@SecurityRequirement(name = "api")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    @Operation(summary = ContentMessages.GET_PUBLISHED_BLOGS)
    public Page<BlogResponse> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getAllBlogs(page, size);
    }

    @GetMapping("/summary")
    @Operation(summary = ContentMessages.GET_BLOG_SUMMARY)
    public List<BlogSummaryDTO> getSummary() {
        return blogService.getAllBlogSummaries();
    }

    @GetMapping("/{id}")
    @Operation(summary = ContentMessages.VIEW_BLOG)
    public BlogResponse viewBlog(@PathVariable Long id) {
        return blogService.viewBlog(id);
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = ContentMessages.GET_BLOG_DETAIL)
    public BlogResponse getBlogDetail(@PathVariable Long id) {
        return blogService.getBlogById(id);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = ContentMessages.LIKE_BLOG)
    public ResponseEntity<String> likeBlog(@PathVariable Long id) {
        blogService.likeBlog(id);
        return ResponseEntity.ok(ContentMessages.LIKE_BLOG_SUCCESS);
    }

    @GetMapping("/by-tag/{tagId}")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_TAG)
    public Page<BlogResponse> getBlogsByTag(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getBlogsByTag(tagId, page, size);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.CREATE_BLOG)
    public BlogResponse createBlog(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam MultipartFile image,
            @RequestParam(required = false) List<String> tags) {
        BlogRequest request = new BlogRequest();
        request.setTitle(title);
        request.setContent(content);
        request.setImg(image);
        request.setTagNames(tags);
        return blogService.createBlog(request);
    }

    @GetMapping("/me")
    @Operation(summary = ContentMessages.GET_MY_BLOGS)
    public Page<BlogResponse> getMyBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getMyBlogs(page, size);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.UPDATE_BLOG)
    public BlogResponse updateBlog(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) List<String> tags) {
        BlogRequest request = new BlogRequest();
        request.setTitle(title);
        request.setContent(content);
        request.setImg(image);
        request.setTagNames(tags);
        return blogService.updateBlog(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = ContentMessages.DELETE_BLOG)
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok(ContentMessages.DELETE_BLOG_SUCCESS);
    }

    @GetMapping("/admin/all")
    @Operation(summary = ContentMessages.GET_BLOGS_FOR_MANAGEMENT)
    public Page<BlogResponse> getAllBlogsForManagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getAllBlogsForManagement(page, size);
    }

    @GetMapping("/admin/by-status")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_STATUS)
    public Page<BlogResponse> getBlogsByStatus(
            @RequestParam BlogStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getBlogsByStatus(status, page, size);
    }

    @PostMapping("/admin/{id}/approve")
    @Operation(summary = ContentMessages.APPROVE_BLOG)
    public BlogResponse approveBlog(@PathVariable Long id) {
        return blogService.approveBlog(id);
    }

    @PostMapping("/admin/{id}/reject")
    @Operation(summary = ContentMessages.REJECT_BLOG)
    public BlogResponse rejectBlog(@PathVariable Long id) {
        return blogService.rejectBlog(id);
    }

    @PostMapping("/admin/{id}/publish")
    @Operation(summary = ContentMessages.PUBLISH_BLOG)
    public BlogResponse publishBlog(@PathVariable Long id) {
        return blogService.publishBlog(id);
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = ContentMessages.SUBMIT_BLOG)
    public BlogResponse submitBlog(@PathVariable Long id) {
        return blogService.submitBlogForReview(id);
    }

    @GetMapping("/me/by-status")
    public Page<BlogResponse> getMyBlogsByStatus(
            @RequestParam BlogStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return blogService.getMyBlogsByStatus(status, page, size);
    }
}
