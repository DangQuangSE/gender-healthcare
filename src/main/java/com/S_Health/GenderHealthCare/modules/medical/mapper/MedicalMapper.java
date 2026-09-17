package com.S_Health.GenderHealthCare.modules.medical.mapper;

import com.S_Health.GenderHealthCare.entity.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class MedicalMapper {
    public MedicalInfoResponse toMedicalInfoResponse(MedicalProfile source) {
        if (source == null) {
            return null;
        }

        return MedicalInfoResponse.builder()
                .id(source.getId())
                .customerId(source.getCustomer() == null ? null : source.getCustomer().getId())
                .serviceId(source.getService() == null ? null : source.getService().getId())
                .allergies(source.getAllergies())
                .chronicConditions(source.getChronicConditions())
                .emergencyContact(source.getEmergencyContact())
                .familyHistory(source.getFamilyHistory())
                .lifestyleNotes(source.getLifestyleNotes())
                .specialNotes(source.getSpecialNotes())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .lastUpdatedBy(source.getLastUpdatedBy())
                .active(source.getIsActive())
                .build();
    }
}
