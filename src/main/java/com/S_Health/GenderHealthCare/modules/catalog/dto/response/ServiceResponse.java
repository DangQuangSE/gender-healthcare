package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import com.S_Health.GenderHealthCare.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private Integer duration;
    private ServiceType type;
    private Double price;
    private Double discountPercent;
    private Boolean isActive;
    private Boolean isCombo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> specializationIds;
    private List<SpecializationResponse> specializations;
    private List<Long> subServiceIds;
}
