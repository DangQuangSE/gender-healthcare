package com.S_Health.GenderHealthCare.modules.catalog.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceQuery {
    private String name;
    private Long specializationId;
}
