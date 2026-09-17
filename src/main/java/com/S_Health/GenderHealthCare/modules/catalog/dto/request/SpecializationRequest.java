package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpecializationRequest {
    @NotBlank(message = "Specialization name is required")
    private String name;

    private String description;
}
