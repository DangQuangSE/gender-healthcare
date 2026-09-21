package com.S_Health.GenderHealthCare.modules.medical.dto.response;

import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicalProfileResponse {
    long id;
    long customer_id;
    long service_id;
    List<Appointment> appointments;
    //String note;
}
