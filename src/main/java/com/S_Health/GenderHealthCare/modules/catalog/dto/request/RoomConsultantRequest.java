package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class RoomConsultantRequest {
    @NotNull(message = CatalogConstants.CONSULTANT_ID_REQUIRED)
    private Long consultantId;

    @NotNull(message = CatalogConstants.WORKING_DAY_REQUIRED)
    private DayOfWeek workingDay;

    @NotNull(message = CatalogConstants.START_TIME_REQUIRED)
    private LocalTime startTime;

    @NotNull(message = CatalogConstants.END_TIME_REQUIRED)
    private LocalTime endTime;
}
