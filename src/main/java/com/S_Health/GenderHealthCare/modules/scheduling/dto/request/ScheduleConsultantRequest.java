package com.S_Health.GenderHealthCare.modules.scheduling.dto.request;

import com.S_Health.GenderHealthCare.modules.scheduling.dto.request.RangeDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleConsultantRequest {
    long consultant_id;
    RangeDate rangeDate;
}
