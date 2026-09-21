package com.S_Health.GenderHealthCare.modules.scheduling.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlotResponse {
    Long slotId;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    int maxBooking;
    int currentBooking;
    int availableBooking;
}
