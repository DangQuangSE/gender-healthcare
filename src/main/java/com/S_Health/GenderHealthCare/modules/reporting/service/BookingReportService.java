package com.S_Health.GenderHealthCare.modules.reporting.service;

import com.S_Health.GenderHealthCare.dto.response.report.BookingReportResponse;
import com.S_Health.GenderHealthCare.dto.response.report.ServiceBookingReportDTO;
import com.S_Health.GenderHealthCare.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingReportService {
    private final AppointmentRepository appointmentRepository;

    public BookingReportService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<ServiceBookingReportDTO> getServiceBookingStats(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long serviceId) {
        return appointmentRepository.getServiceBookingReport(startDate, endDate, serviceId);
    }

    public BookingReportResponse getServiceBookingSummary(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return appointmentRepository.getBookingSummary(startDate, endDate);
    }
}
