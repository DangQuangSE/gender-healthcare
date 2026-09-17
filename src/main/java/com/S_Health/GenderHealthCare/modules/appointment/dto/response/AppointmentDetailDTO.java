package com.S_Health.GenderHealthCare.modules.appointment.dto.response;

import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.catalog.dto.response.SimpleRoomDTO;
import com.S_Health.GenderHealthCare.modules.medical.dto.response.ResultDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class AppointmentDetailDTO {
    long id;
    long serviceId;
    String serviceName;
    long consultantId;
    String consultantName;
    LocalDateTime slotTime;
    String joinUrl;
    String startUrl;
    AppointmentStatus status;
    ResultDTO medicalResult;
    SimpleRoomDTO room;
}
