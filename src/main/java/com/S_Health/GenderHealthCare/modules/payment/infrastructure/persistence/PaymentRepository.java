package com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.payment.domain.Payment;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentStatus;
import com.S_Health.GenderHealthCare.modules.payment.enums.PaymentIntent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByAppointmentIdAndStatus(Long appointmentId, PaymentStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p FROM Payment p
        WHERE p.appointment.id = :appointmentId
          AND p.paymentIntent = :paymentIntent
          AND p.status = :status
        """)
    Optional<Payment> findByAppointmentIdAndPaymentIntentAndStatus(
            Long appointmentId,
            PaymentIntent paymentIntent,
            PaymentStatus status
    );
    Optional<Payment> findByAppointmentId(Long appointmentId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status <> :status")
    BigDecimal getTotalRevenueExcludeStatus(@Param("status") PaymentStatus status);

    @Query(value = """
        SELECT COALESCE(SUM(p.amount), 0)
        FROM payment p
        WHERE p.status <> :status AND DATE(p.created_at) = CURRENT_DATE
        """, nativeQuery = true)
    BigDecimal getTodayRevenue(@Param("status") PaymentStatus status);

    @Query(value = """
        SELECT COALESCE(SUM(p.amount), 0)
        FROM payment p
        WHERE p.status <> :status
          AND YEAR(p.created_at) = YEAR(CURRENT_DATE)
          AND MONTH(p.created_at) = MONTH(CURRENT_DATE)
        """, nativeQuery = true)
    BigDecimal getCurrentMonthRevenue(@Param("status") PaymentStatus status);

    @Query(value = """
        SELECT COALESCE(SUM(p.amount), 0)
        FROM payment p
        WHERE p.status <> :status AND YEAR(p.created_at) = YEAR(CURRENT_DATE)
        """, nativeQuery = true)
    BigDecimal getCurrentYearRevenue(@Param("status") PaymentStatus status);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.status <> :status AND p.createdAt BETWEEN :startDate AND :endDate
        """)
    BigDecimal getRevenueByDateRange(
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
