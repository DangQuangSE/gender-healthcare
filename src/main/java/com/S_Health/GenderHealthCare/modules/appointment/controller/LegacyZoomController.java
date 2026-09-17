package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.integrations.zoom.ZoomMeetingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/zoom")
@SecurityRequirement(name = "api")
public class LegacyZoomController {
    @Autowired
    private ZoomMeetingService zoomMeetingService;

    @GetMapping("/test-create-meeting")
    public Map<String, String> testCreateMeeting(@RequestParam Long appointmentId) {
        return zoomMeetingService.createMeeting(appointmentId);
    }
}
