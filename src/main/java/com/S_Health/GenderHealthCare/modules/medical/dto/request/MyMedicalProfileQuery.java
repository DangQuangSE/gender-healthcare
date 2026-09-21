package com.S_Health.GenderHealthCare.modules.medical.dto.request;

import jakarta.validation.constraints.NotNull;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyMedicalProfileQuery {
    @NotNull(message = MedicalMessages.SERVICE_ID_REQUIRED)
    private Long serviceId;
}
