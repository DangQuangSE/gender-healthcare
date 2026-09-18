package com.S_Health.GenderHealthCare.modules.scheduling.dto.response;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceDetailResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleServiceResponse {
    ServiceDetailResponse serviceDTO;
    List<WorkDateSlotResponse> scheduleResponses;
}
