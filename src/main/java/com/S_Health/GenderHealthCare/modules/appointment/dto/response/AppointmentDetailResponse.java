package com.S_Health.GenderHealthCare.modules.appointment.dto.response;

import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomResponse;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalResultResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class AppointmentDetailResponse {
    long id;
    long serviceId;
    String serviceName;
    long consultantId;
    String consultantName;
    LocalDateTime slotTime;
    String joinUrl;
    String startUrl;
    AppointmentStatus status;
    MedicalResultResponse medicalResult;
    SimpleRoomResponse room;
}
