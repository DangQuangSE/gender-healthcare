package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {
    @NotBlank(message = "Room name is required")
    private String name;

    private String description;

    @NotNull(message = "Specialization ID is required")
    private Long specializationId;
}
