package com.S_Health.GenderHealthCare.modules.scheduling.dto.response;

import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.SlotResponse;
import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorWorkingScheduleResponse {
    private UserDetailResponse doctor;
    private LocalDate workDate;
    private List<SlotResponse> slots;
}
