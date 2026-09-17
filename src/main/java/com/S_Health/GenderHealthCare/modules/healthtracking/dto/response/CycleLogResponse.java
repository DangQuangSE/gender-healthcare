package com.S_Health.GenderHealthCare.modules.healthtracking.dto.response;

import com.S_Health.GenderHealthCare.modules.healthtracking.enums.Symptoms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleLogResponse {
    private Long userId;
    private LocalDate startDate;
    private Boolean periodStart;
    private List<Symptoms> symptoms;
    private String note;
}
