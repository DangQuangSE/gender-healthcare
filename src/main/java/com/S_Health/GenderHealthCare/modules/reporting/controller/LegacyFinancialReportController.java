package com.S_Health.GenderHealthCare.modules.reporting.controller;

import com.S_Health.GenderHealthCare.modules.reporting.service.FinancialReportService;
import com.S_Health.GenderHealthCare.modules.reporting.dto.request.FinancialReportQuery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/financial-reports")
public class LegacyFinancialReportController {
    @Autowired
    FinancialReportService financialReportService;

    @GetMapping("/revenue-today")
    public BigDecimal getTodayRevenue() {
        return financialReportService.getTodayRevenue();
    }

    @GetMapping("/revenue-month")
    public BigDecimal getCurrentMonthRevenue() {
        return financialReportService.getCurrentMonthRevenue();
    }

    @GetMapping("/revenue-year")
    public BigDecimal getCurrentYearRevenue() {
        return financialReportService.getCurrentYearRevenue();
    }

    @GetMapping("/revenue-by-date-range")
    public BigDecimal getRevenueByDateRange(
            @Valid @ModelAttribute FinancialReportQuery request
    ) {
        return financialReportService.getRevenueByDateRange(request);
    }
}
