package com.S_Health.GenderHealthCare.modules.medical.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.appointment.domain.AppointmentDetail;
import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalResultRepository extends JpaRepository<MedicalResult, Long> {
    Optional<MedicalResult> findByIdAndIsActiveTrue(Long id);
    List<MedicalResult> findAllByAppointmentDetailIdAndIsActiveTrue(Long appointmentDetailId);
    Optional<MedicalResult> findByAppointmentDetail(AppointmentDetail appointmentDetail);
    List<MedicalResult> findByAppointmentDetailIn(List<AppointmentDetail> appointmentDetails);

    @Query("""
            select case when count(result) > 0 then true else false end
            from MedicalResult result
            join result.appointmentDetail detail
            join detail.appointment appointment
            where result.isActive = true
              and detail.isActive = true
              and appointment.isActive = true
              and result.treatmentProtocol.id = :protocolId
              and appointment.customer.id = :customerId
            """)
    boolean existsActiveByTreatmentProtocolIdAndCustomerId(
            @Param("protocolId") Long protocolId,
            @Param("customerId") Long customerId);
}
