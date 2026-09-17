package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.enums.ServiceType;
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
    @NotBlank(message = "Service name is required")
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
