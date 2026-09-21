package com.S_Health.GenderHealthCare.modules.reporting.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueGroupResponse {
    String serviceCategory;
    Integer year;
    Integer month;
    BigDecimal totalAmount;
}
