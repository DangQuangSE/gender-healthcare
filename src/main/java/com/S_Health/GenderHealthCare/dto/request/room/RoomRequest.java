package com.S_Health.GenderHealthCare.dto.request.room;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {
    @NotBlank(message = CatalogConstants.ROOM_NAME_REQUIRED)
    String name;
    String description;
    @NotNull(message = CatalogConstants.SPECIALIZATION_ID_REQUIRED)
    Long specializationId;
}
