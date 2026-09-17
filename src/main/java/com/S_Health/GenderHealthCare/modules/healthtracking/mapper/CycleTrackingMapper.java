package com.S_Health.GenderHealthCare.modules.healthtracking.mapper;

import com.S_Health.GenderHealthCare.dto.request.service.CycleTrackingRequest;
import com.S_Health.GenderHealthCare.modules.healthtracking.dto.response.CycleLogResponse;
import org.springframework.stereotype.Component;

@Component
public class CycleTrackingMapper {
    public CycleLogResponse toResponse(CycleTrackingRequest source) {
        if (source == null) {
            return null;
        }

        return CycleLogResponse.builder()
                .userId(source.getUserId())
                .startDate(source.getStartDate())
                .periodStart(source.getIsPeriodStart())
                .symptoms(source.getSymptoms())
                .note(source.getNote())
                .build();
    }
}
