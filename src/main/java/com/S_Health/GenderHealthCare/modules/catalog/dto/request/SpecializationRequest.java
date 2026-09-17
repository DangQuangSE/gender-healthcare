package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecializationRequest {
    @NotBlank(message = CatalogConstants.SPECIALIZATION_NAME_REQUIRED)
    private String name;

    private String description;
}
