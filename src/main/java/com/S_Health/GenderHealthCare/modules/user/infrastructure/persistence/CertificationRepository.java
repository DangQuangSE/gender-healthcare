package com.S_Health.GenderHealthCare.modules.user.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.user.domain.Certification;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByConsultantAndIsActiveTrue(User consultant);

    List<Certification> findByConsultantIdAndIsActiveTrue(Long consultantId);

    Optional<Certification> findByIdAndConsultantAndIsActiveTrue(Long id, User consultant);
}
