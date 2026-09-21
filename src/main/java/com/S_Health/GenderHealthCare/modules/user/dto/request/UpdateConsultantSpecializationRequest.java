package com.S_Health.GenderHealthCare.modules.user.dto.request;

import com.S_Health.GenderHealthCare.modules.user.UserMessages;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConsultantSpecializationRequest {
    @NotEmpty(message = UserMessages.SPECIALIZATIONS_REQUIRED)
    private Set<Long> specializationIds;
}
