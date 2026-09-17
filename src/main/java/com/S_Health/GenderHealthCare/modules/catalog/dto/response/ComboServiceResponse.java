package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboServiceResponse {
    private ServiceResponse comboService;
    private List<ServiceResponse> subServices;
}
