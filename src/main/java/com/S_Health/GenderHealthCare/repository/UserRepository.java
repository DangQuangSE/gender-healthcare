package com.S_Health.GenderHealthCare.repository;

import com.S_Health.GenderHealthCare.modules.user.domain.User;
import com.S_Health.GenderHealthCare.modules.user.enums.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndRole(Long id, UserRole role);

}
