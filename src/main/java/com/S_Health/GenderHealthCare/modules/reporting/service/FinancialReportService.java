package com.S_Health.GenderHealthCare.modules.reporting.service;

import com.S_Health.GenderHealthCare.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class FinancialReportService {
    private final PaymentRepository paymentRepository;

    public FinancialReportService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public BigDecimal getTodayRevenue() {
        return paymentRepository.getTodayRevenue(PaymentStatus.FAILED);
    }

    public BigDecimal getCurrentMonthRevenue() {
        return paymentRepository.getCurrentMonthRevenue(PaymentStatus.FAILED);
    }

    public BigDecimal getCurrentYearRevenue() {
        return paymentRepository.getCurrentYearRevenue(PaymentStatus.FAILED);
    }

    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.getRevenueByDateRange(
                PaymentStatus.FAILED,
                startDate.atStartOfDay(),
                endDate.atTime(23,59,59)
        );
    }


}
