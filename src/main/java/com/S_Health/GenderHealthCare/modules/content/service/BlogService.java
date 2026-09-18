package com.S_Health.GenderHealthCare.modules.content.service;

import com.S_Health.GenderHealthCare.modules.content.domain.Blog;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Tag;
import com.S_Health.GenderHealthCare.modules.content.enums.BlogStatus;


import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import com.S_Health.GenderHealthCare.modules.content.dto.request.BlogRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.response.BlogResponse;
import com.S_Health.GenderHealthCare.modules.content.dto.response.BlogSummaryResponse;
import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.repository.BlogRepository;
import com.S_Health.GenderHealthCare.integrations.storage.ImageStorage;
import com.S_Health.GenderHealthCare.modules.catalog.service.TagService;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final ImageStorage imageStorage;
    private final AuthUtil authUtil;
    private final TagService tagService;

    public BlogService(
            BlogRepository blogRepository,
            ModelMapper modelMapper,
            ImageStorage imageStorage,
            AuthUtil authUtil,
            TagService tagService) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.imageStorage = imageStorage;
        this.authUtil = authUtil;
        this.tagService = tagService;
    }

    @Transactional
    public BlogResponse viewBlog(long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));
        blog.setViewCount(blog.getViewCount() + 1);
        BlogResponse blogRp = modelMapper.map(blog, BlogResponse.class);
        return blogRp;
    }

    @Transactional
    public void likeBlog(long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));
        blog.setLikeCount(blog.getLikeCount() + 1);
        blogRepository.save(blog);
    }

    public List<BlogSummaryResponse> getAllBlogSummaries() {
        return blogRepository.findAllBlogSummaries();
    }

    @Transactional
    public BlogResponse createBlog(BlogRequest request) {
        validateBlogRequest(request);
        // Lấy user từ JWT
        User author = authUtil.getCurrentUser();

        // Tự động set status dựa trên role của user
        BlogStatus blogStatus;
        if (author.getRole() == UserRole.ADMIN) {
            blogStatus = BlogStatus.PUBLISHED; // Admin tạo blog sẽ được publish luôn
        } else {
            blogStatus = BlogStatus.PENDING; // User thường tạo blog sẽ ở trạng thái pending
        }

        // Upload image to Cloudinary if provided
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = imageStorage.uploadImage(request.getImage());
                request.setImgUrl(imageUrl);
            } catch (IOException e) {
                throw new DomainException(ErrorCode.INTERNAL_ERROR, ContentMessages.IMAGE_UPLOAD_FAILED, e);
            }
        }

        // 3. Tạo blog
        Blog blog = new Blog();
        blog.setTitle(request.getTitle().trim());
        blog.setContent(request.getContent().trim());
        blog.setImgUrl(request.getImgUrl());

        // Sử dụng status đã được tính toán tự động dựa trên role
        blog.setStatus(blogStatus);
        blog.setAuthor(author);

        // Process tags if provided - SỬ DỤNG METHOD MỚI
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            // Kiểm tra tag không hợp lệ
            List<String> invalidTags = tagService.validateTagNames(request.getTagNames());
            if (!invalidTags.isEmpty()) {
                throw new DomainException(ContentMessages.INVALID_TAGS.formatted(String.join(", ", invalidTags)));
            }

            // Lấy các tag đã tồn tại
            List<Tag> tags = tagService.getExistingTags(request.getTagNames());
            blog.setTags(tags);
        }

        blogRepository.save(blog);
        // 4. Lưu vào DB
        BlogResponse blogResponse = modelMapper.map(blog, BlogResponse.class);
        blogResponse.setAuthor(modelMapper.map(author, UserDetailResponse.class));
        return blogResponse;
    }

    public Page<BlogResponse> getAllBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogRepository.findAllPublishedBlogs(pageable);
        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            if (blog.getAuthor() != null) {
                response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            }
            return response;
        });
    }

    public Page<BlogResponse> getAllBlogsForManagement(int page, int size) {
        User currentUser = authUtil.getCurrentUser();

        // Chỉ admin hoặc staff mới có thể xem tất cả blog
        if (!isStaffOrAdmin(currentUser.getRole())) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.VIEW_ALL_BLOGS_FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogRepository.findAll(pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            if (blog.getAuthor() != null) {
                response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            }
            return response;
        });
    }

    public Page<BlogResponse> getBlogsByTag(Long tagId, int page, int size) {

        tagService.getTagById(tagId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogRepository.findByTagId(tagId, pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            if (blog.getAuthor() != null) {
                response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            }
            return response;
        });
    }

    public Page<BlogResponse> getBlogsByTags(List<Long> tagIds, int page, int size) {
        if (tagIds == null || tagIds.isEmpty()) {
            throw new DomainException(ErrorCode.BAD_REQUEST, ContentMessages.BLOG_TAGS_REQUIRED);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogRepository.findByTagIds(tagIds, pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            if (blog.getAuthor() != null) {
                response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            }
            return response;
        });
    }

    @Transactional
    public BlogResponse updateBlog(Long blogId, BlogRequest request) {
        validateBlogRequest(request);

        // Tìm blog
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        // Kiểm tra quyền sở hữu (admin có thể chỉnh sửa bất kỳ blog nào)
        User currentUser = authUtil.getCurrentUser();
        if (currentUser.getRole() != UserRole.ADMIN &&
                !(blog.getAuthor() != null
                        && Objects.equals(blog.getAuthor().getId(), currentUser.getId()))) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.UPDATE_BLOG_FORBIDDEN);
        }

        // Upload hình ảnh mới nếu có
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                String imageUrl = imageStorage.uploadImage(request.getImage());
                blog.setImgUrl(imageUrl);
            } catch (IOException e) {
                throw new DomainException(ErrorCode.INTERNAL_ERROR, ContentMessages.IMAGE_UPLOAD_FAILED, e);
            }
        } else if (request.getImgUrl() != null) {
            blog.setImgUrl(request.getImgUrl());
        }

        // Cập nhật thông tin blog (không cho phép cập nhật status)
        blog.setTitle(request.getTitle().trim());
        blog.setContent(request.getContent().trim());

        // Cập nhật tags nếu có - SỬ DỤNG METHOD MỚI
        if (request.getTagNames() != null) {
            // Kiểm tra tag không hợp lệ
            List<String> invalidTags = tagService.validateTagNames(request.getTagNames());
            if (!invalidTags.isEmpty()) {
                throw new DomainException(ContentMessages.INVALID_TAGS.formatted(String.join(", ", invalidTags)));
            }
            // Lấy các tag đã tồn tại
            List<Tag> tags = tagService.getExistingTags(request.getTagNames());
            blog.setTags(tags);
        }

        blogRepository.save(blog);

        BlogResponse blogResponse = modelMapper.map(blog, BlogResponse.class);
        blogResponse.setAuthor(modelMapper.map(currentUser, UserDetailResponse.class));
        return blogResponse;
    }

    private void validateBlogRequest(BlogRequest request) {
        if (request == null || request.getTitle() == null || request.getContent() == null) {
            throw new DomainException(ErrorCode.BAD_REQUEST, ContentMessages.BLOG_CONTENT_REQUIRED);
        }
        if (request.getTitle().trim().length() < 10) {
            throw new DomainException(ErrorCode.BAD_REQUEST, ContentMessages.BLOG_TITLE_TOO_SHORT);
        }
        if (request.getContent().trim().length() < 50) {
            throw new DomainException(ErrorCode.BAD_REQUEST, ContentMessages.BLOG_CONTENT_TOO_SHORT);
        }
    }

    @Transactional
    public void deleteBlog(Long blogId) {
        // Tìm blog
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        // Kiểm tra quyền sở hữu
        User currentUser = authUtil.getCurrentUser();
        if (!isStaffOrAdmin(currentUser.getRole())) {
            if (!(blog.getAuthor() != null
                    && Objects.equals(blog.getAuthor().getId(), currentUser.getId()))) {
                throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.DELETE_BLOG_FORBIDDEN);
            }
        }

        // Xóa blog
        blogRepository.delete(blog);
    }

    public Page<BlogResponse> getMyBlogs(int page, int size) {
        User currentUser = authUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Thêm method mới vào BlogRepository
        Page<Blog> blogs = blogRepository.findByAuthorOrderByCreatedAtDesc(currentUser, pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            return response;
        });
    }

    public BlogResponse getBlogById(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        if (blog.getAuthor() != null) {
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
        }
        return response;
    }

    public Page<BlogResponse> getMyBlogsByStatus(BlogStatus status, int page, int size) {
        User currentUser = authUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Thêm method mới vào BlogRepository
        Page<Blog> blogs = blogRepository.findByAuthorAndStatusOrderByCreatedAtDesc(currentUser, status, pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            return response;
        });
    }

    public Page<BlogResponse> getBlogsByStatus(BlogStatus status, int page, int size) {
        // Chỉ admin/staff mới có thể xem blog theo status
        User currentUser = authUtil.getCurrentUser();
        if (!isStaffOrAdmin(currentUser.getRole())) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.VIEW_STATUS_BLOGS_FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Blog> blogs = blogRepository.findByStatusOrderByCreatedAtDesc(status, pageable);

        return blogs.map(blog -> {
            BlogResponse response = modelMapper.map(blog, BlogResponse.class);
            if (blog.getAuthor() != null) {
                response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
            }
            return response;
        });
    }

    @Transactional
    public BlogResponse approveBlog(Long blogId) {
        User currentUser = authUtil.getCurrentUser();
        if (!isStaffOrAdmin(currentUser.getRole())) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.APPROVE_BLOG_FORBIDDEN);
        }

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        if (blog.getStatus() != BlogStatus.PENDING) {
            throw new DomainException(ContentMessages.APPROVE_PENDING_ONLY);
        }

        blog.setStatus(BlogStatus.PUBLISHED);
        blogRepository.save(blog);
        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        if (blog.getAuthor() != null) {
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
        }
        return response;
    }

    @Transactional
    public BlogResponse rejectBlog(Long blogId) {
        User currentUser = authUtil.getCurrentUser();
        if (!isStaffOrAdmin(currentUser.getRole())) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.REJECT_BLOG_FORBIDDEN);
        }

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        if (blog.getStatus() != BlogStatus.PENDING) {
            throw new DomainException(ContentMessages.REJECT_PENDING_ONLY);
        }

        blog.setStatus(BlogStatus.REJECTED);
        blogRepository.save(blog);
        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        if (blog.getAuthor() != null) {
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
        }
        return response;
    }

    @Transactional
    public BlogResponse publishBlog(Long blogId) {
        User currentUser = authUtil.getCurrentUser();
        if (!isStaffOrAdmin(currentUser.getRole())) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.PUBLISH_BLOG_FORBIDDEN);
        }

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));


        blog.setStatus(BlogStatus.PUBLISHED);
        blogRepository.save(blog);

        log.info(ContentMessages.BLOG_PUBLISHED_LOG, blogId, currentUser.getId());

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        if (blog.getAuthor() != null) {
            response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
        }
        return response;
    }

    // Thêm method submit blog cho author
    @Transactional
    public BlogResponse submitBlogForReview(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DomainException(ErrorCode.NOT_FOUND, ContentMessages.BLOG_NOT_FOUND));

        User currentUser = authUtil.getCurrentUser();
        if (!(blog.getAuthor() != null
                && Objects.equals(blog.getAuthor().getId(), currentUser.getId()))) {
            throw new DomainException(ErrorCode.FORBIDDEN, ContentMessages.SUBMIT_BLOG_FORBIDDEN);
        }
        blog.setStatus(BlogStatus.PENDING);
        blogRepository.save(blog);

        log.info(ContentMessages.BLOG_SUBMITTED_LOG, blogId, currentUser.getId());

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);
        response.setAuthor(modelMapper.map(blog.getAuthor(), UserDetailResponse.class));
        return response;
    }

    private boolean isStaffOrAdmin(UserRole role) {
        return role == UserRole.STAFF
                || role == UserRole.ADMIN
                || role == UserRole.SUPER_ADMIN;
    }
}
