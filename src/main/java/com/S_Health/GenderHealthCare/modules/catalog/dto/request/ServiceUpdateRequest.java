package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUpdateRequest {
    private String name;
    private String description;
    private Integer duration;
    private ServiceType type;
    private Double price;
    private Double discountPercent;
    private List<Long> specializationIds;
}
