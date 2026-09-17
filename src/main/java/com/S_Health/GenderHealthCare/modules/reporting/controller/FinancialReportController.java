package com.S_Health.GenderHealthCare.modules.reporting.controller;

import com.S_Health.GenderHealthCare.modules.reporting.ReportMessages;
import com.S_Health.GenderHealthCare.modules.reporting.service.FinancialReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports/financial")
@SecurityRequirement(name = "api")
public class FinancialReportController {
    private final FinancialReportService financialReportService;

    public FinancialReportController(FinancialReportService financialReportService) {
        this.financialReportService = financialReportService;
    }

    @GetMapping("/today")
    @Operation(summary = ReportMessages.GET_REVENUE_TODAY)
    public BigDecimal getTodayRevenue() {
        return financialReportService.getTodayRevenue();
    }

    @GetMapping("/month")
    @Operation(summary = ReportMessages.GET_REVENUE_MONTH)
    public BigDecimal getCurrentMonthRevenue() {
        return financialReportService.getCurrentMonthRevenue();
    }

    @GetMapping("/year")
    @Operation(summary = ReportMessages.GET_REVENUE_YEAR)
    public BigDecimal getCurrentYearRevenue() {
        return financialReportService.getCurrentYearRevenue();
    }

    @GetMapping("/range")
    @Operation(summary = ReportMessages.GET_REVENUE_BY_RANGE)
    public BigDecimal getRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return financialReportService.getRevenueByDateRange(startDate, endDate);
    }
}
