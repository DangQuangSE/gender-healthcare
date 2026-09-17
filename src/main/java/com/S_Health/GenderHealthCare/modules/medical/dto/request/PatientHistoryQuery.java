package com.S_Health.GenderHealthCare.modules.medical.dto.request;

import jakarta.validation.constraints.Min;
import com.S_Health.GenderHealthCare.modules.medical.MedicalMessages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientHistoryQuery {
    @Min(value = 0, message = MedicalMessages.PAGE_NOT_NEGATIVE)
    private int page = 0;

    @Min(value = 1, message = MedicalMessages.PAGE_SIZE_INVALID)
    private int size = 5;
}
