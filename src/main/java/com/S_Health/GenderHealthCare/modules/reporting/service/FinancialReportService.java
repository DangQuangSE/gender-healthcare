package com.S_Health.GenderHealthCare.modules.reporting.service;

import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.reporting.dto.request.FinancialReportQuery;


import com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public BigDecimal getRevenueByDateRange(FinancialReportQuery request) {
        return paymentRepository.getRevenueByDateRange(
                PaymentStatus.FAILED,
                request.getStartDate().atStartOfDay(),
                request.getEndDate().atTime(23,59,59)
        );
    }


}
