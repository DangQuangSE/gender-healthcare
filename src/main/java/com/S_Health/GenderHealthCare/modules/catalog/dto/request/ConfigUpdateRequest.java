package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import jakarta.validation.constraints.NotNull;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigUpdateRequest {
    @NotNull(message = CatalogConstants.CONFIG_VALUE_REQUIRED)
    private Integer value;
}
