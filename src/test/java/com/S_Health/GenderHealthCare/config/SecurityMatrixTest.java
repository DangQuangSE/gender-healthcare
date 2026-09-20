package com.S_Health.GenderHealthCare.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityMatrixTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void anonymousUserCannotReadMedicalResults() throws Exception {
        mockMvc.perform(get("/api/v1/medical-results/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerCannotReadProtectedMedicalResults() throws Exception {
        mockMvc.perform(get("/api/v1/medical-results/1").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void PayOSWebhookIsPublicToTheJwtFilterButStillValidatesItsSignature() throws Exception {
        mockMvc.perform(post("/api/v1/payments/payos/webhook")
                        .contentType("application/json")
                        .content("{\"code\":\"00\",\"success\":true,\"data\":{},\"signature\":\"invalid\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void PayOSPaymentCreationStillRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/payments/payos")
                        .contentType("application/json")
                        .content("{\"appointmentId\":1}"))
                .andExpect(status().isUnauthorized());
    }
}
