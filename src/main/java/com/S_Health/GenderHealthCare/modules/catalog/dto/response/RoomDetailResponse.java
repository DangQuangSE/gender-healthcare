package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SpecializationDetailResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class RoomDetailResponse {
    private long id;
    private String name;
    private String description;
    private SpecializationDetailResponse specialization;
    private List<RoomConsultantDetailResponse> consultants;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
