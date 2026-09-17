package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.modules.appointment.service.ZoomMeetingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/zoom")
@SecurityRequirement(name = "api")
public class LegacyZoomController {
    private final ZoomMeetingService zoomMeetingService;

    public LegacyZoomController(ZoomMeetingService zoomMeetingService) {
        this.zoomMeetingService = zoomMeetingService;
    }

    @GetMapping("/test-create-meeting")
    public Map<String, String> testCreateMeeting(@RequestParam Long appointmentId) {
        return zoomMeetingService.createMeeting(appointmentId);
    }
}
