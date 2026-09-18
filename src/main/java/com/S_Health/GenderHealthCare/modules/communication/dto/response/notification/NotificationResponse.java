package com.S_Health.GenderHealthCare.modules.communication.dto.response.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    Long id;
    String title;
    String content;
    @Builder.Default
    Boolean isRead = false;
    @Builder.Default
    Boolean isActive = true;
    String type;
    LocalDateTime createdAt;
    LocalDateTime readAt;
    NotificationAppointmentResponse appointment;
    NotificationCycleTrackingResponse cycleTracking;

}
