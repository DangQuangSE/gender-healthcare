package com.S_Health.GenderHealthCare.modules.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConsultantSpecializationRequest {
    @NotEmpty(message = "Danh sách ID chuyên môn không được để trống")
    private Set<Long> specializationIds;
}
