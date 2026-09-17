package com.S_Health.GenderHealthCare.modules.payment.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayResponse {
    Long amount;
    String status;
    String message;
    String oderInfo;
    String URL;
    int responseCode;
}
