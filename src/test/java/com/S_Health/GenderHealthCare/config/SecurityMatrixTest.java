package com.S_Health.GenderHealthCare.config;

import com.S_Health.GenderHealthCare.common.message.ApiResponseMessages;
import com.S_Health.GenderHealthCare.modules.catalog.CatalogConstants;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.AUTHENTICATION_REQUIRED))
                .andExpect(jsonPath("$.requestId").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerCannotReadProtectedMedicalResults() throws Exception {
        mockMvc.perform(get("/api/v1/medical-results/1").with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.ACCESS_DENIED));
    }

    @Test
    void malformedPathVariableUsesTheCommonErrorEnvelope() throws Exception {
        mockMvc.perform(get("/api/v1/services/not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.BAD_REQUEST))
                .andExpect(jsonPath("$.path").value("/api/v1/services/not-a-number"));
    }

    @Test
    void versionedJsonBodyUsesTheCommonSuccessEnvelope() throws Exception {
        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value(ApiResponseMessages.SUCCESS_CODE))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void legacyJsonBodyUsesTheSameCommonSuccessEnvelope() throws Exception {
        mockMvc.perform(get("/api/services").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value(ApiResponseMessages.SUCCESS_CODE))
                .andExpect(jsonPath("$.message").value(ApiResponseMessages.SUCCESS_MESSAGE))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void domainExceptionUsesItsExplicitClientMessage() throws Exception {
        long missingServiceId = 999999L;

        mockMvc.perform(get("/api/v1/services/{id}", missingServiceId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value(CatalogConstants.SERVICE_NOT_FOUND.formatted(missingServiceId)))
                .andExpect(jsonPath("$.path").value("/api/v1/services/999999"));
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
