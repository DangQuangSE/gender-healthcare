package com.S_Health.GenderHealthCare.modules.communication.dto.response.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCycleTrackingResponse {
    Long id;
    LocalDate cycleStartDate;
    Integer duration;
}
