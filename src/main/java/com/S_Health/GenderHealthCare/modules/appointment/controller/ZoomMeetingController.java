package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.modules.appointment.AppointmentMessages;
import com.S_Health.GenderHealthCare.modules.appointment.service.ZoomMeetingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointments")
public class ZoomMeetingController {
    private final ZoomMeetingService zoomMeetingService;

    public ZoomMeetingController(ZoomMeetingService zoomMeetingService) {
        this.zoomMeetingService = zoomMeetingService;
    }

    @PostMapping("/{appointmentId}/meeting")
    @Operation(summary = AppointmentMessages.CREATE_MEETING)
    public ApiResponse<Map<String, String>> createMeeting(@PathVariable Long appointmentId) {
        return ApiResponse.success(zoomMeetingService.createMeeting(appointmentId), null);
    }
}
