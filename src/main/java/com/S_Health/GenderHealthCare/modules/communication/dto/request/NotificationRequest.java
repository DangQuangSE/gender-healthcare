package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import com.S_Health.GenderHealthCare.modules.communication.CommunicationMessages;
import com.S_Health.GenderHealthCare.modules.communication.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    @NotBlank(message = CommunicationMessages.NOTIFICATION_TITLE_REQUIRED)
    String title;

    @NotBlank(message = CommunicationMessages.NOTIFICATION_CONTENT_REQUIRED)
    String content;

    @NotNull(message = CommunicationMessages.NOTIFICATION_TYPE_REQUIRED)
    NotificationType type;

    @Positive(message = CommunicationMessages.NOTIFICATION_APPOINTMENT_ID_POSITIVE)
    Long appointmentId;

    @Positive(message = CommunicationMessages.NOTIFICATION_CYCLE_ID_POSITIVE)
    Long cycleTrackingId;
}
