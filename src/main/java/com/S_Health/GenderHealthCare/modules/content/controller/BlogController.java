package com.S_Health.GenderHealthCare.modules.content.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;

import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogCreateRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogPageQuery;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogStatusQuery;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogTagQuery;
import com.S_Health.GenderHealthCare.modules.content.dto.response.BlogResponse;
import com.S_Health.GenderHealthCare.modules.content.dto.response.BlogSummaryResponse;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.modules.content.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @Valid @ModelAttribute BlogPageQuery request) {
        return blogService.getAllBlogs(request.getPage(), request.getSize());
    }

    @GetMapping("/summary")
    @Operation(summary = ContentMessages.GET_BLOG_SUMMARY)
    public List<BlogSummaryResponse> getSummary() {
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
    public ApiResponse<String> likeBlog(@PathVariable Long id) {
        blogService.likeBlog(id);
        return ApiResponse.success(ContentMessages.LIKE_BLOG_SUCCESS, null);
    }

    @GetMapping("/by-tag/{tagId}")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_TAG)
    public Page<BlogResponse> getBlogsByTag(
            @PathVariable Long tagId,
            @Valid @ModelAttribute BlogPageQuery request) {
        return blogService.getBlogsByTag(tagId, request.getPage(), request.getSize());
    }

    @GetMapping("/by-tags")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_TAG)
    public Page<BlogResponse> getBlogsByTags(
            @Valid @ModelAttribute BlogTagQuery request) {
        return blogService.getBlogsByTags(request.getTags(), request.getPage(), request.getSize());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.CREATE_BLOG)
    public BlogResponse createBlog(@Valid @ModelAttribute BlogCreateRequest request) {
        return blogService.createBlog(request);
    }

    @GetMapping("/me")
    @Operation(summary = ContentMessages.GET_MY_BLOGS)
    public Page<BlogResponse> getMyBlogs(
            @Valid @ModelAttribute BlogPageQuery request) {
        return blogService.getMyBlogs(request.getPage(), request.getSize());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = ContentMessages.UPDATE_BLOG)
    public BlogResponse updateBlog(
            @PathVariable Long id,
            @Valid @ModelAttribute BlogRequest request) {
        return blogService.updateBlog(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = ContentMessages.DELETE_BLOG)
    public ApiResponse<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ApiResponse.success(ContentMessages.DELETE_BLOG_SUCCESS, null);
    }

    @GetMapping("/admin/all")
    @Operation(summary = ContentMessages.GET_BLOGS_FOR_MANAGEMENT)
    public Page<BlogResponse> getAllBlogsForManagement(
            @Valid @ModelAttribute BlogPageQuery request) {
        return blogService.getAllBlogsForManagement(request.getPage(), request.getSize());
    }

    @GetMapping("/admin/by-status")
    @Operation(summary = ContentMessages.GET_BLOGS_BY_STATUS)
    public Page<BlogResponse> getBlogsByStatus(
            @Valid @ModelAttribute BlogStatusQuery request) {
        return blogService.getBlogsByStatus(request.getStatus(), request.getPage(), request.getSize());
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
            @Valid @ModelAttribute BlogStatusQuery request) {
        return blogService.getMyBlogsByStatus(request.getStatus(), request.getPage(), request.getSize());
    }
}
