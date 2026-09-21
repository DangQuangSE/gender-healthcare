package com.S_Health.GenderHealthCare.modules.reporting.controller;

import com.S_Health.GenderHealthCare.modules.reporting.dto.response.BookingReportResponse;
import com.S_Health.GenderHealthCare.modules.reporting.dto.response.ServiceBookingReportResponse;
import com.S_Health.GenderHealthCare.modules.reporting.ReportMessages;
import com.S_Health.GenderHealthCare.modules.reporting.dto.request.BookingReportQuery;
import com.S_Health.GenderHealthCare.modules.reporting.service.BookingReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/bookings")
@SecurityRequirement(name = "api")
public class BookingReportController {
    private final BookingReportService bookingReportService;

    public BookingReportController(BookingReportService bookingReportService) {
        this.bookingReportService = bookingReportService;
    }

    @GetMapping("/stats")
    @Operation(summary = ReportMessages.GET_BOOKING_STATS)
    public List<ServiceBookingReportResponse> getStats(
            @Valid @ModelAttribute BookingReportQuery request) {
        return bookingReportService.getServiceBookingStats(request);
    }

    @GetMapping("/summary")
    @Operation(summary = ReportMessages.GET_BOOKING_SUMMARY)
    public BookingReportResponse getSummary(
            @Valid @ModelAttribute BookingReportQuery request) {
        return bookingReportService.getServiceBookingSummary(request);
    }
}
