package com.S_Health.GenderHealthCare.modules.payment;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FreePaymentMethodMigrationContractTest {

    private static final Path MIGRATIONS = Path.of("docs/migrations");
    private static final String PAYMENT_METHOD_ENUM =
            "ENUM('MOMO', 'PAY_OFF', 'PAYOS', 'VN_PAY', 'FREE')";

    @Test
    void freshPayOsMigrationAcceptsFreePaymentMethod() throws IOException {
        String migration = readMigration("payos-payment-data-v1.sql");

        assertThat(migration).contains(PAYMENT_METHOD_ENUM);
    }

    @Test
    void upgradeMigrationAddsFreePaymentMethodForExistingPayOsInstallations() throws IOException {
        String migration = readMigration("payos-payment-data-v2-free-payment-method.sql");

        assertThat(migration)
                .contains("ALTER TABLE payment")
                .contains(PAYMENT_METHOD_ENUM)
                .contains("WHERE method = 'FREE'");
    }

    @Test
    void rollbackGuardsBothPayOsAndFreePaymentRows() throws IOException {
        String rollback = readMigration("payos-payment-data-v2-free-payment-method-rollback.sql");

        assertThat(rollback)
                .contains("WHERE method IN ('PAYOS', 'FREE')")
                .contains("ENUM('MOMO', 'PAY_OFF', 'PAYOS', 'VN_PAY')");
    }

    private String readMigration(String fileName) throws IOException {
        return Files.readString(MIGRATIONS.resolve(fileName));
    }
}
