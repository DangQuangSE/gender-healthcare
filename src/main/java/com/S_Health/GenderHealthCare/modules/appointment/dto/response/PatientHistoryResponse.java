package com.S_Health.GenderHealthCare.modules.appointment.dto.response;

import com.S_Health.GenderHealthCare.modules.medical.dto.response.MedicalProfileResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistoryResponse {
    private MedicalProfileResponse medicalProfile;
    private List<AppointmentResponse> pastAppointments;
}
