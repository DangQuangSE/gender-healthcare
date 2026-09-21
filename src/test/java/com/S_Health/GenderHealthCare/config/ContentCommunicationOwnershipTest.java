package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentCommunicationOwnershipTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");

    @Test
    void contentAndCommunicationPersistenceIsModuleOwned() {
        assertOwnedBy("modules/content/infrastructure/persistence", "BlogRepository.java");
        assertOwnedBy("modules/content/infrastructure/persistence", "CommentRepository.java");
        assertOwnedBy("modules/communication/infrastructure/persistence", "ChatMessageRepository.java");
        assertOwnedBy("modules/communication/infrastructure/persistence", "ChatSessionRepository.java");
        assertOwnedBy("modules/communication/infrastructure/persistence", "NotificationRepository.java");

        for (String fileName : new String[]{
                "BlogRepository.java", "CommentRepository.java", "ChatMessageRepository.java",
                "ChatSessionRepository.java", "NotificationRepository.java"}) {
            assertFalse(Files.exists(SOURCE_ROOT.resolve("repository").resolve(fileName)), fileName);
        }
    }

    private static void assertOwnedBy(String directory, String fileName) {
        assertTrue(Files.exists(SOURCE_ROOT.resolve(directory).resolve(fileName)), fileName);
    }
}
