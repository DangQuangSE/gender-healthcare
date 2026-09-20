package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalRepositoryCleanupTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");

    @Test
    void noGlobalRepositoryPackageRemainsAfterOwnershipMigration() {
        assertTrue(Files.exists(SOURCE_ROOT.resolve("modules/scheduling/infrastructure/persistence/ConsultantSlotRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/ConsultantSlotRepository.java")));
        try (var files = Files.list(SOURCE_ROOT.resolve("repository"))) {
            assertTrue(files.findAny().isEmpty(), "global repository package still contains adapters");
        } catch (java.io.IOException exception) {
            throw new AssertionError("cannot inspect global repository package", exception);
        }
    }
}
