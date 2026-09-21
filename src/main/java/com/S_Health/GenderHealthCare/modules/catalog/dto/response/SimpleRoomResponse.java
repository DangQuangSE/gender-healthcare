package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleRoomResponse {
    long id;
    String name;
    String description;
    String specializationName;
}
