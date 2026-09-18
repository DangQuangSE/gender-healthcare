package com.S_Health.GenderHealthCare.modules.appointment.dto.response;

import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;

import com.S_Health.GenderHealthCare.modules.appointment.dto.response.AppointmentDetailResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    long appointmentId;
    String customerName;
    LocalDate date;
    LocalTime time;
    String note;
    AppointmentStatus status;
    List<AppointmentDetailResponse> details;
}
