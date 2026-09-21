package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import com.S_Health.GenderHealthCare.modules.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomConsultantResponse {
    private long id;
    private UserResponse consultant;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
