package com.S_Health.GenderHealthCare.dto.request.tag;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    @NotBlank(message = CatalogConstants.TAG_NAME_REQUIRED)
    private String name;

    private String description;
}
