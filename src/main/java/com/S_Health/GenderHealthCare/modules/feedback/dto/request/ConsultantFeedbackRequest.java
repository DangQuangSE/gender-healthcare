package com.S_Health.GenderHealthCare.modules.feedback.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultantFeedbackRequest {
    double rating;
    Long serviceFeedbackId;
    Long consultantId;
    String comment;
}
