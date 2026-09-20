package com.S_Health.GenderHealthCare.modules.appointment.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.appointment.domain.Appointment;
import com.S_Health.GenderHealthCare.modules.appointment.enums.AppointmentStatus;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.reporting.dto.response.BookingReportResponse;
import com.S_Health.GenderHealthCare.modules.reporting.dto.response.ServiceBookingReportResponse;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByMedicalProfileId(Long medicalProfileId);
    List<Appointment> findByMedicalProfileIdAndStatus(Long medicalProfileId, AppointmentStatus status);
    List<Appointment> findByPreferredDateAndConsultantAndStatusAndIsActiveTrue(LocalDate date, User consultant, AppointmentStatus status);
    List<Appointment> findByPreferredDateAndConsultantAndIsActiveTrue(LocalDate date, User consultant);
    List<Appointment> findByMedicalProfileAndStatusAndIsActiveTrue(MedicalProfile medicalProfile, AppointmentStatus status);
    List<Appointment> findByCustomerAndStatusAndIsActiveTrue(User customer, AppointmentStatus status);
    List<Appointment> findByPreferredDateAndStatusInAndIsActiveTrue(LocalDate preferredDate, List<AppointmentStatus> statuses);
    List<Appointment> findByPreferredDateAndIsActiveTrue(LocalDate preferredDate);
    List<Appointment> findByConsultantAndStatusAndIsActiveTrue(User customer, AppointmentStatus status);
    List<Appointment> findByStatusAndIsActiveTrue(AppointmentStatus status);

    @Query("""
        SELECT new com.S_Health.GenderHealthCare.modules.reporting.dto.response.ServiceBookingReportResponse(
            a.service.id, a.service.name,
            SUM(CASE WHEN a.status IN ('PENDING','CONFIRMED','PROCESSING','COMPLETED','CHECKED','ABSENT') THEN 1 ELSE 0 END),
            SUM(CASE WHEN a.status IN ('CANCELED','DELETED') THEN 1 ELSE 0 END)
        )
        FROM Appointment a
        WHERE a.created_at BETWEEN :startDate AND :endDate
          AND (:serviceId IS NULL OR a.service.id = :serviceId)
          AND a.isActive = true
        GROUP BY a.service.id, a.service.name
        """)
    List<ServiceBookingReportResponse> getServiceBookingReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("serviceId") Long serviceId
    );

    @Query("""
        SELECT new com.S_Health.GenderHealthCare.modules.reporting.dto.response.BookingReportResponse(
            SUM(CASE WHEN a.status IN ('PENDING','CONFIRMED','PROCESSING','COMPLETED','CHECKED') THEN 1 ELSE 0 END),
            SUM(CASE WHEN a.status IN ('CANCELED','DELETED') THEN 1 ELSE 0 END)
        )
        FROM Appointment a
        WHERE a.created_at BETWEEN :startDate AND :endDate
          AND a.isActive = true
        """)
    BookingReportResponse getBookingSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Appointment> findByServiceIdAndIsRatedTrue(Long serviceId);
}
