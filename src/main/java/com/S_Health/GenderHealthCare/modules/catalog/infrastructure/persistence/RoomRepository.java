package com.S_Health.GenderHealthCare.modules.catalog.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.catalog.domain.Room;
import com.S_Health.GenderHealthCare.modules.catalog.domain.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findBySpecialization(Specialization specialization);
    List<Room> findByIsActiveTrue();
    List<Room> findBySpecializationAndIsActiveTrue(Specialization specialization);
    boolean existsByNameAndIsActiveTrue(String name);
}
