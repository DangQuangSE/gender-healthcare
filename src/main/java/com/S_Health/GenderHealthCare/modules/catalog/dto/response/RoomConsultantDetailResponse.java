package com.S_Health.GenderHealthCare.modules.catalog.dto.response;

import com.S_Health.GenderHealthCare.modules.user.dto.response.UserDetailResponse;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class RoomConsultantDetailResponse {
    private long id;
    private UserDetailResponse consultant;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
