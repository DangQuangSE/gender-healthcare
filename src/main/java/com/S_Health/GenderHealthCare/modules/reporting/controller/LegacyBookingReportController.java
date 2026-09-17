package com.S_Health.GenderHealthCare.modules.reporting.controller;

import com.S_Health.GenderHealthCare.dto.response.report.BookingReportResponse;
import com.S_Health.GenderHealthCare.dto.response.report.ServiceBookingReportDTO;
import com.S_Health.GenderHealthCare.modules.reporting.dto.request.BookingReportQuery;
import com.S_Health.GenderHealthCare.modules.reporting.service.BookingReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/booking-reports")
public class LegacyBookingReportController {
    @Autowired
    BookingReportService bookingReportService;

    @GetMapping("/stats")
    public ResponseEntity<List<ServiceBookingReportDTO>> getServiceBookingStats(
            @Valid @ModelAttribute BookingReportQuery request
    ) {
        List<ServiceBookingReportDTO> report = bookingReportService.getServiceBookingStats(request);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/summary")
    public ResponseEntity<BookingReportResponse> getServiceBookingSummary(
            @Valid @ModelAttribute BookingReportQuery request
    ) {
        BookingReportResponse summary = bookingReportService.getServiceBookingSummary(request);
        return ResponseEntity.ok(summary);
    }
}
