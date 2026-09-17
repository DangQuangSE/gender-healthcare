package com.S_Health.GenderHealthCare.modules.scheduling.dto.response;

import com.S_Health.GenderHealthCare.modules.scheduling.dto.response.SlotDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkDateSlotResponse {
     LocalDate workDate;
     List<SlotDTO> slots;
}
