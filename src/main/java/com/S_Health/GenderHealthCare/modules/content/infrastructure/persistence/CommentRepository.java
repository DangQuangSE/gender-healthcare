package com.S_Health.GenderHealthCare.modules.content.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.content.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlogId(Long blogId);
}
