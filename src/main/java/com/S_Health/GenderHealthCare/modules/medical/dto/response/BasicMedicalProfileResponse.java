package com.S_Health.GenderHealthCare.modules.medical.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicMedicalProfileResponse {
    String allergies;                    // Dị ứng thuốc/thực phẩm
    String familyHistory;                // Tiền sử gia đình
    String chronicConditions;            // Bệnh mãn tính
    String specialNotes;                 // Ghi chú đặc biệt từ staff
}
