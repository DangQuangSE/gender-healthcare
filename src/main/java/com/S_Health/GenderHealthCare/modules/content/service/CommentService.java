package com.S_Health.GenderHealthCare.modules.content.service;

import com.S_Health.GenderHealthCare.modules.content.domain.Blog;
import com.S_Health.GenderHealthCare.modules.content.domain.Comment;
import com.S_Health.GenderHealthCare.modules.user.domain.User;


import com.S_Health.GenderHealthCare.modules.content.dto.request.CommentRequest;
import com.S_Health.GenderHealthCare.modules.content.dto.response.CommentResponse;
import com.S_Health.GenderHealthCare.exception.exceptions.AppException;
import com.S_Health.GenderHealthCare.modules.content.ContentMessages;
import com.S_Health.GenderHealthCare.repository.AuthenticationRepository;
import com.S_Health.GenderHealthCare.repository.BlogRepository;
import com.S_Health.GenderHealthCare.repository.CommentRepository;
import com.S_Health.GenderHealthCare.utils.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final AuthenticationRepository authenticationRepository;
    private final AuthUtil authUtil;

    public CommentService(
            CommentRepository commentRepository,
            BlogRepository blogRepository,
            AuthenticationRepository authenticationRepository,
            AuthUtil authUtil) {
        this.commentRepository = commentRepository;
        this.blogRepository = blogRepository;
        this.authenticationRepository = authenticationRepository;
        this.authUtil = authUtil;
    }

    public CommentResponse createComment(CommentRequest request) {
        Blog blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new AppException(ContentMessages.COMMENT_BLOG_NOT_FOUND));

        Long userId = authUtil.getCurrentUserId();
        User commenter = authenticationRepository.findById(userId)
                .orElseThrow(() -> new AppException(ContentMessages.USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .blog(blog)
                .commenter(commenter)
                .description(request.getDescription())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Convert to CommentResponse
        return new CommentResponse(
                savedComment.getId(),
                savedComment.getCommenter().getFullname() != null ? savedComment.getCommenter().getFullname() : ContentMessages.UNKNOWN_USER,
                savedComment.getCommenter().getId(),
                savedComment.getCommenter().getImageUrl(),
                savedComment.getDescription(),
                savedComment.getCreateAt()
        );
    }

    public List<CommentResponse> getCommentsByBlog(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogId(blogId);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getCommenter().getFullname() != null ? comment.getCommenter().getFullname() : ContentMessages.UNKNOWN_USER,
                        comment.getCommenter().getId(),
                        comment.getCommenter().getImageUrl(),
                        comment.getDescription(),
                        comment.getCreateAt()
                ))
                .toList();
    }

    public void deleteComment(Long commentID) {
        User user = authUtil.getCurrentUser();
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new AppException(ContentMessages.COMMENT_NOT_FOUND));
        if (comment.getCommenter().getId() != user.getId()) {
            throw new AppException(ContentMessages.DELETE_COMMENT_FORBIDDEN);
        }
        commentRepository.delete(comment);
    }
}
