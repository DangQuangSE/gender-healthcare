package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.modules.appointment.dto.request.BookingRequest;
import com.S_Health.GenderHealthCare.modules.appointment.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/booking")
@SecurityRequirement(name = "api")
@Deprecated(since = "1.0", forRemoval = false)
public class LegacyBookingController {
    private final BookingService bookingService;

    public LegacyBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @PostMapping("/medicalService")
    public ResponseEntity bookAppointment(
            @RequestBody BookingRequest request
    ) {
        return ResponseEntity.ok(bookingService.bookingService(request));
    }
}
