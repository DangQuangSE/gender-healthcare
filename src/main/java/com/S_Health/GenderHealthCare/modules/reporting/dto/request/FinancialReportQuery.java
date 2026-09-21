package com.S_Health.GenderHealthCare.modules.reporting.dto.request;

import jakarta.validation.constraints.NotNull;
import com.S_Health.GenderHealthCare.modules.reporting.ReportMessages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class FinancialReportQuery {
    @NotNull(message = ReportMessages.START_DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = ReportMessages.END_DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
