package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestProfileResourceTest {

    @Test
    void testProfileUsesExplicitJdbcAndControlledSchema() throws IOException {
        ClassPathResource resource = new ClassPathResource("application-test.yml");

        assertTrue(resource.exists(), "application-test.yml must define an isolated test profile");

        String configuration = resource.getContentAsString(StandardCharsets.UTF_8);

        assertTrue(configuration.contains("url:"), "test profile must define an explicit JDBC URL");
        assertTrue(configuration.contains("ddl-auto: create-drop"),
                "test profile must use a disposable schema strategy");
        assertFalse(configuration.contains("${DB_URL}"),
                "test profile must not depend on the developer .env");
    }
}
