package com.S_Health.GenderHealthCare.modules.communication.dto.response.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationAppointmentResponse {
    Long id;
    String doctorName;
    String serviceName;
    LocalDate appointmentDate;
}
