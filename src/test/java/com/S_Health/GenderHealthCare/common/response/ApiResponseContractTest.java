package com.S_Health.GenderHealthCare.common.response;

import com.S_Health.GenderHealthCare.common.exception.ErrorCode;
import com.S_Health.GenderHealthCare.common.message.ApiResponseMessages;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseContractTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void successResponseUsesTheStableEnvelope() throws Exception {
        ApiResponse<String> response = ApiResponse.success("value", "/api/v1/example");

        assertThat(response.success()).isTrue();
        assertThat(response.status()).isEqualTo(200);
        assertThat(response.code()).isEqualTo(ApiResponseMessages.SUCCESS_CODE);
        assertThat(response.message()).isEqualTo(ApiResponseMessages.SUCCESS_MESSAGE);
        assertThat(response.data()).isEqualTo("value");
        assertThat(response.path()).isEqualTo("/api/v1/example");

        Map<String, Object> json = objectMapper.readValue(
                objectMapper.writeValueAsString(response),
                new TypeReference<>() {
                });
        assertThat(json.keySet()).contains("timestamp", "success", "status", "code", "message", "data", "path");
        assertThat(json.keySet()).doesNotContain("errors", "meta", "requestId");
    }

    @Test
    void errorResponseKeepsOnlySafeNonBlankFieldErrors() {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("email", "Email không hợp lệ.");
        errors.put("empty", "");
        errors.put("missing", null);

        ApiResponse<Void> response = ApiResponse.error(
                ErrorCode.VALIDATION_ERROR.getStatus(),
                ErrorCode.VALIDATION_ERROR.name(),
                ErrorCode.VALIDATION_ERROR.getDefaultMessage(),
                errors,
                "/api/v1/auth/login");

        assertThat(response.success()).isFalse();
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.code()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.errors()).containsExactly(Map.entry("email", "Email không hợp lệ."));
        assertThat(response.data()).isNull();
    }
}
