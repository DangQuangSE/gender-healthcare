package com.S_Health.GenderHealthCare.modules.appointment.dto.request;

import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentScheduleQuery {
    @NotNull(message = AppointmentMessages.APPOINTMENT_DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private AppointmentStatus status;
}
