package com.S_Health.GenderHealthCare.modules.reporting.dto.request;

import jakarta.validation.constraints.NotNull;
import com.S_Health.GenderHealthCare.modules.reporting.ReportMessages;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingReportQuery {
    @NotNull(message = ReportMessages.START_DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull(message = ReportMessages.END_DATE_REQUIRED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private Long serviceId;

    // Keep the legacy snake_case query names working during migration.
    public void setStart_date(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEnd_date(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setService_id(Long serviceId) {
        this.serviceId = serviceId;
    }
}
