package com.S_Health.GenderHealthCare.modules.feedback.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.feedback.domain.ConsultantFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultantFeedbackRepository extends JpaRepository<ConsultantFeedback, Long> {
    List<ConsultantFeedback> findByServiceFeedbackId(Long feedbackId);
    List<ConsultantFeedback> findByConsultantId(Long consultantId);
    List<ConsultantFeedback> findByServiceFeedbackIdIn(List<Long> serviceFeedbackIds);
}
