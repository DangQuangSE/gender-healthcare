package com.S_Health.GenderHealthCare.modules.reporting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceBookingReportResponse {
    Long serviceId;
    String serviceName;
    Long totalBookings;
    Long totalCancellations;
}
