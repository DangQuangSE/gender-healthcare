package com.S_Health.GenderHealthCare.modules.scheduling.dto.response;

import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleServiceResponse {
    ServiceDTO serviceDTO;
    List<WorkDateSlotResponse> scheduleResponses;
}
