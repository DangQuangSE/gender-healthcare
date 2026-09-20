package com.S_Health.GenderHealthCare.modules.payment.infrastructure.persistence;

import com.S_Health.GenderHealthCare.modules.payment.domain.Transaction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByOrderId(String orderId);
    Optional<Transaction> findByProviderOrderCode(Long providerOrderCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transaction t WHERE t.providerOrderCode = :providerOrderCode")
    Optional<Transaction> findByProviderOrderCodeForUpdate(@Param("providerOrderCode") Long providerOrderCode);
    Optional<Transaction> findByProviderPaymentLinkId(String providerPaymentLinkId);
}
