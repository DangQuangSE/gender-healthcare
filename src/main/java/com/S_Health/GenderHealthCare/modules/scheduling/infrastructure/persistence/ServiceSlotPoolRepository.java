package com.S_Health.GenderHealthCare.modules.scheduling.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.scheduling.domain.ServiceSlotPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ServiceSlotPoolRepository extends JpaRepository<ServiceSlotPool, Long> {
    Optional<ServiceSlotPool> findByService_idAndDateAndStartTime(Long serviceId, LocalDate date, LocalTime startTime);
}
