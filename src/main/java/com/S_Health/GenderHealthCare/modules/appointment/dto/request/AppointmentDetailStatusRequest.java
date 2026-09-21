package com.S_Health.GenderHealthCare.modules.appointment.dto.request;

import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDetailStatusRequest {
    @NotNull(message = AppointmentMessages.DETAIL_STATUS_REQUIRED)
    private AppointmentStatus status;
}
