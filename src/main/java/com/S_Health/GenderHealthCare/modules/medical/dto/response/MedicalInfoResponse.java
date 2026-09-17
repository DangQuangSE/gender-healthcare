package com.S_Health.GenderHealthCare.modules.medical.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalInfoResponse {
    private Long id;
    private Long customerId;
    private Long serviceId;
    private String allergies;
    private String chronicConditions;
    private String emergencyContact;
    private String familyHistory;
    private String lifestyleNotes;
    private String specialNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long lastUpdatedBy;
    private Boolean active;
}
