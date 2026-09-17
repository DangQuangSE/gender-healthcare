package com.S_Health.GenderHealthCare.modules.appointment.controller;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import com.S_Health.GenderHealthCare.dto.request.service.BookingRequest;
import com.S_Health.GenderHealthCare.dto.response.BookingResponse;
import com.S_Health.GenderHealthCare.service.MedicalService.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Create an appointment booking")
    public ApiResponse<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {
        return ApiResponse.success(bookingService.bookingService(request), null);
    }
}
