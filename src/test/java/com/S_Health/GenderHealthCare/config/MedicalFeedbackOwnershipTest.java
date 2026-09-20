package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MedicalFeedbackOwnershipTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");

    @Test
    void medicalCycleAndFeedbackAdaptersAreModuleOwned() {
        assertOwnedBy("modules/medical/infrastructure/persistence", "MedicalProfileRepository.java");
        assertOwnedBy("modules/medical/infrastructure/persistence", "MedicalResultRepository.java");
        assertOwnedBy("modules/medical/infrastructure/persistence", "TreatmentProtocolRepository.java");
        assertOwnedBy("modules/healthtracking/infrastructure/persistence", "CycleTrackingRepository.java");
        assertOwnedBy("modules/feedback/infrastructure/persistence", "ServiceFeedbackRepository.java");
        assertOwnedBy("modules/feedback/infrastructure/persistence", "ConsultantFeedbackRepository.java");

        for (String fileName : new String[]{
                "MedicalProfileRepository.java", "MedicalResultRepository.java",
                "TreatmentProtocolRepository.java", "CycleTrackingRepository.java",
                "ServiceFeedbackRepository.java", "ConsultantFeedbackRepository.java"}) {
            assertFalse(Files.exists(SOURCE_ROOT.resolve("repository").resolve(fileName)), fileName);
        }
    }

    private static void assertOwnedBy(String directory, String fileName) {
        assertTrue(Files.exists(SOURCE_ROOT.resolve(directory).resolve(fileName)), fileName);
    }
}
