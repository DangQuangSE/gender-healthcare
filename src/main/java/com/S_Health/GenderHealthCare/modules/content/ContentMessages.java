package com.S_Health.GenderHealthCare.modules.content;

/**
 * User-facing messages and API descriptions for content features.
 */
public final class ContentMessages {
    public static final String BLOG_NOT_FOUND = "Không tìm thấy bài viết";
    public static final String IMAGE_UPLOAD_FAILED = "Không thể tải lên hình ảnh";
    public static final String INVALID_TAGS = "Các tag sau không tồn tại: %s";
    public static final String VIEW_ALL_BLOGS_FORBIDDEN = "Bạn không có quyền xem tất cả blog";
    public static final String UPDATE_BLOG_FORBIDDEN = "Bạn không có quyền chỉnh sửa bài viết này";
    public static final String DELETE_BLOG_FORBIDDEN = "Bạn không có quyền xóa bài viết này";
    public static final String VIEW_STATUS_BLOGS_FORBIDDEN = "Bạn không có quyền xem danh sách blog theo trạng thái";
    public static final String APPROVE_BLOG_FORBIDDEN = "Bạn không có quyền duyệt bài viết";
    public static final String APPROVE_PENDING_ONLY = "Chỉ có thể duyệt bài viết có trạng thái PENDING";
    public static final String REJECT_BLOG_FORBIDDEN = "Bạn không có quyền từ chối bài viết";
    public static final String REJECT_PENDING_ONLY = "Chỉ có thể từ chối bài viết có trạng thái PENDING";
    public static final String PUBLISH_BLOG_FORBIDDEN = "Bạn không có quyền đăng bài viết";
    public static final String SUBMIT_BLOG_FORBIDDEN = "Bạn không có quyền gửi bài viết này để duyệt";
    public static final String COMMENT_BLOG_NOT_FOUND = "Không tìm thấy blog hoặc không có blog";
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String UNKNOWN_USER = "Unknown User";
    public static final String COMMENT_NOT_FOUND = "Không tìm thấy bình luận";
    public static final String DELETE_COMMENT_FORBIDDEN = "Bạn không có quyền xóa bình luận này";

    public static final String VIEW_BLOG = "View blog details and increase view count";
    public static final String LIKE_BLOG = "Like a blog post";
    public static final String GET_BLOG_SUMMARY = "Get published blog summaries";
    public static final String GET_PUBLISHED_BLOGS = "Get published blogs";
    public static final String CREATE_BLOG = "Create a blog post";
    public static final String GET_MY_BLOGS = "Get my blog posts";
    public static final String GET_BLOG_DETAIL = "Get blog detail without increasing the view count";
    public static final String GET_BLOGS_BY_TAG = "Get blogs by tag";
    public static final String GET_BLOGS_FOR_MANAGEMENT = "Get blogs for management";
    public static final String GET_BLOGS_BY_STATUS = "Get blogs by status";
    public static final String APPROVE_BLOG = "Approve a blog post";
    public static final String REJECT_BLOG = "Reject a blog post";
    public static final String PUBLISH_BLOG = "Publish a blog post";
    public static final String SUBMIT_BLOG = "Submit a blog post for review";
    public static final String GET_COMMENTS = "Get comments for a blog";
    public static final String CREATE_COMMENT = "Create a blog comment";
    public static final String DELETE_COMMENT = "Delete a blog comment";
    public static final String UPDATE_BLOG = "Update a blog post";
    public static final String DELETE_BLOG = "Delete a blog post";
    public static final String DELETE_BLOG_SUCCESS = "Đã xóa bài viết thành công";
    public static final String LIKE_BLOG_SUCCESS = "Đã thả tim cho bài viết!";
    public static final String DELETE_COMMENT_SUCCESS = "Xóa bình luận thành công";

    public static final String BLOG_TITLE_TOO_SHORT = "Blog title must contain at least 10 characters";
    public static final String BLOG_CONTENT_TOO_SHORT = "Blog content must contain at least 50 characters";
    public static final String BLOG_IMAGE_REQUIRED = "Image file is required";
    public static final String BLOG_TITLE_REQUIRED = "Title is required";
    public static final String BLOG_CONTENT_REQUIRED = "Content is required";
    public static final String BLOG_PUBLISHED_LOG = "Blog {} was published by admin {}";
    public static final String BLOG_SUBMITTED_LOG = "Blog {} was submitted for review by author {}";

    private ContentMessages() {
    }
}
