package com.S_Health.GenderHealthCare.repository;

import com.S_Health.GenderHealthCare.modules.medical.domain.MedicalProfile;
import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalProfileRepository extends JpaRepository<MedicalProfile, Long> {
    Optional<MedicalProfile> findByCustomerAndIsActiveTrue(User currentUser);
    Optional<MedicalProfile> findByCustomerAndServiceAndIsActiveTrue(User currentUser, Service service);
    List<MedicalProfile> findByCustomerIdAndIsActiveTrue(Long customerId);

}
