package com.S_Health.GenderHealthCare.modules.payment.dto.request;

import com.S_Health.GenderHealthCare.modules.payment.PaymentMessages;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = PaymentMessages.APPOINTMENT_ID_REQUIRED)
    @Positive(message = PaymentMessages.APPOINTMENT_ID_POSITIVE)
    private Long appointmentId;
}
