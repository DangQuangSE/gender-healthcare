package com.S_Health.GenderHealthCare.modules.catalog.dto.response;


import com.S_Health.GenderHealthCare.modules.catalog.dto.response.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComboResponse {
    private ServiceDTO comboService;
    private List<ServiceDTO> subServices;
}
