package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryOwnershipTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");
    private static final Path USER_PERSISTENCE = SOURCE_ROOT.resolve("modules/user/infrastructure/persistence");

    @Test
    void userRepositoriesBelongToUserInfrastructureBoundary() {
        assertTrue(Files.exists(USER_PERSISTENCE.resolve("UserRepository.java")));
        assertTrue(Files.exists(USER_PERSISTENCE.resolve("AuthenticationRepository.java")));
        assertTrue(Files.exists(USER_PERSISTENCE.resolve("CertificationRepository.java")));

        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/UserRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/AuthenticationRepository.java")));
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository/CertificationRepository.java")));
    }
}
