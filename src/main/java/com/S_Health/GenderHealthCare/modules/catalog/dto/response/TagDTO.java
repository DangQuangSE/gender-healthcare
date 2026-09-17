package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private String description;
}
