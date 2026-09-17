package com.S_Health.GenderHealthCare.modules.communication.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequest {
    Long userId;
    String title;
    String content;
    String type;
    Long appointmentId;
    Long cycleTrackingId;
}
