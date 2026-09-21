package com.S_Health.GenderHealthCare.modules.scheduling.dto.request;

import jakarta.validation.constraints.NotNull;
import com.S_Health.GenderHealthCare.modules.scheduling.SchedulingMessages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class WorkingDoctorRequest {
    @NotNull(message = SchedulingMessages.DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
