package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank(message = CatalogConstants.ROOM_NAME_REQUIRED)
    private String name;

    private String description;

    @NotNull(message = CatalogConstants.SPECIALIZATION_ID_REQUIRED)
    private Long specializationId;
}
