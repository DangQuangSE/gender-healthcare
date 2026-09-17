package com.S_Health.GenderHealthCare.modules.reporting.service;



import com.S_Health.GenderHealthCare.dto.response.report.BookingReportResponse;
import com.S_Health.GenderHealthCare.dto.response.report.ServiceBookingReportDTO;
import com.S_Health.GenderHealthCare.modules.reporting.dto.request.BookingReportQuery;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingReportService {
    private final AppointmentRepository appointmentRepository;

    public BookingReportService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<ServiceBookingReportDTO> getServiceBookingStats(BookingReportQuery request) {
        return appointmentRepository.getServiceBookingReport(
                request.getStartDate(),
                request.getEndDate(),
                request.getServiceId());
    }

    public BookingReportResponse getServiceBookingSummary(BookingReportQuery request) {
        return appointmentRepository.getBookingSummary(request.getStartDate(), request.getEndDate());
    }
}
