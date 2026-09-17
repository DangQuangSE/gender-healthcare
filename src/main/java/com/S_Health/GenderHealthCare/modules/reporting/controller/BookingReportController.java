package com.S_Health.GenderHealthCare.modules.reporting.controller;

import com.S_Health.GenderHealthCare.dto.response.report.BookingReportResponse;
import com.S_Health.GenderHealthCare.dto.response.report.ServiceBookingReportDTO;
import com.S_Health.GenderHealthCare.modules.reporting.ReportMessages;
import com.S_Health.GenderHealthCare.modules.reporting.service.BookingReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
    public List<ServiceBookingReportDTO> getStats(
            @RequestParam("start_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("end_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(value = "service_id", required = false) Long serviceId) {
        return bookingReportService.getServiceBookingStats(startDate, endDate, serviceId);
    }

    @GetMapping("/summary")
    @Operation(summary = ReportMessages.GET_BOOKING_SUMMARY)
    public BookingReportResponse getSummary(
            @RequestParam("start_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("end_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return bookingReportService.getServiceBookingSummary(startDate, endDate);
    }
}
