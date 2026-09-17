package com.S_Health.GenderHealthCare.modules.scheduling.dto.request;

import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.RangeDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleServiceRequest {
    long service_id;
    RangeDate rangeDate;
}
