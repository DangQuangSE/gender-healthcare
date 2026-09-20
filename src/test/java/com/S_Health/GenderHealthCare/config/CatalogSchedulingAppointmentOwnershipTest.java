package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogSchedulingAppointmentOwnershipTest {

    private static final Path SOURCE_ROOT = Path.of("src/main/java/com/S_Health/GenderHealthCare");

    @Test
    void persistenceAdaptersLiveWithTheirVerticalSlice() {
        assertOwnedBy("modules/appointment/infrastructure/persistence", "AppointmentRepository.java");
        assertOwnedBy("modules/appointment/infrastructure/persistence", "AppointmentDetailRepository.java");
        assertOwnedBy("modules/scheduling/infrastructure/persistence", "ScheduleRepository.java");
        assertOwnedBy("modules/scheduling/infrastructure/persistence", "ServiceSlotPoolRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "ServiceRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "SpecializationRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "TagRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "RoomRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "RoomConsultantRepository.java");
        assertOwnedBy("modules/catalog/infrastructure/persistence", "ComboItemRepository.java");

        assertRootRepositoryRemoved("AppointmentRepository.java");
        assertRootRepositoryRemoved("AppointmentDetailRepository.java");
        assertRootRepositoryRemoved("ScheduleRepository.java");
        assertRootRepositoryRemoved("ServiceSlotPoolRepository.java");
        assertRootRepositoryRemoved("ServiceRepository.java");
        assertRootRepositoryRemoved("SpecializationRepository.java");
        assertRootRepositoryRemoved("TagRepository.java");
        assertRootRepositoryRemoved("RoomRepository.java");
        assertRootRepositoryRemoved("RoomConsultantRepository.java");
        assertRootRepositoryRemoved("ComboItemRepository.java");
    }

    private static void assertOwnedBy(String directory, String fileName) {
        assertTrue(Files.exists(SOURCE_ROOT.resolve(directory).resolve(fileName)), fileName);
    }

    private static void assertRootRepositoryRemoved(String fileName) {
        assertFalse(Files.exists(SOURCE_ROOT.resolve("repository").resolve(fileName)), fileName);
    }
}
