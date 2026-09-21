package com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.catalog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndIsActiveTrue(String name);
    boolean existsByName(String name);
}
