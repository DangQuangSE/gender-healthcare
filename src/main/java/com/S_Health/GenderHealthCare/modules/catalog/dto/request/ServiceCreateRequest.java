package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import com.S_Health.GenderHealthCare.modules.catalog.enums.ServiceType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreateRequest {
    @NotBlank(message = CatalogConstants.SERVICE_NAME_REQUIRED)
    private String name;
    private String description;
    private Integer duration;
    private ServiceType type;
    private Double price;
    private Double discountPercent;
    private Boolean isCombo;
    private List<Long> specializationIds;
    private List<Long> subServiceIds;
}
