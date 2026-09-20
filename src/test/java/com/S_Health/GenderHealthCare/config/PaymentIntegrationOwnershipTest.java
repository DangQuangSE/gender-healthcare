package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentIntegrationOwnershipTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");

    @Test
    void paymentPersistenceIsOwnedByPaymentAnd_catalog_configByCatalog() {
        assertTrue(Files.exists(SOURCE_ROOT.resolve("modules/payment/infrastructure/persistence/PaymentRepository.java")));
        assertTrue(Files.exists(SOURCE_ROOT.resolve("modules/payment/infrastructure/persistence/TransactionRepository.java")));
        assertTrue(Files.exists(SOURCE_ROOT.resolve("modules/catalog/infrastructure/persistence/ConfigValueRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/PaymentRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/TransactionRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/ConfigValueRepository.java")));
    }
}
