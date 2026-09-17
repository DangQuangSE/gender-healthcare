package com.S_Health.GenderHealthCare.modules.feedback.dto.request;

import com.S_Health.GenderHealthCare.modules.feedback.FeedbackMessages;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceFeedbackRequest {
    @Min(value = 1, message = FeedbackMessages.RATING_INVALID)
    @Max(value = 5, message = FeedbackMessages.RATING_INVALID)
    double rating;

    @Size(max = 1000, message = FeedbackMessages.COMMENT_TOO_LONG)
    String comment;

    @Size(max = 1000, message = FeedbackMessages.CONSULTANT_COMMENT_TOO_LONG)
    String commentConsultant;

    @NotNull(message = FeedbackMessages.APPOINTMENT_ID_REQUIRED)
    @Positive(message = FeedbackMessages.APPOINTMENT_ID_POSITIVE)
    Long appointmentId;
}
