package com.S_Health.GenderHealthCare.common.response;

import com.S_Health.GenderHealthCare.common.filter.RequestIdFilter;
import com.S_Health.GenderHealthCare.common.message.ApiResponseMessages;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Common response envelope for normal JSON responses.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Instant timestamp,
        boolean success,
        int status,
        String code,
        String message,
        T data,
        Object meta,
        Map<String, String> errors,
        String path,
        String requestId) {

    public static <T> ApiResponse<T> success(T data, String path) {
        return success(HttpStatus.OK, ApiResponseMessages.SUCCESS_CODE, ApiResponseMessages.SUCCESS_MESSAGE, data, path);
    }

    public static <T> ApiResponse<T> success(
            HttpStatus status,
            String code,
            String message,
            T data,
            String path) {
        return new ApiResponse<>(
                Instant.now(),
                true,
                status.value(),
                code,
                message,
                data,
                null,
                null,
                resolvePath(path),
                currentRequestId());
    }

    public static ApiResponse<List<?>> paged(Page<?> page, String path) {
        return paged(HttpStatus.OK, ApiResponseMessages.SUCCESS_CODE, ApiResponseMessages.SUCCESS_MESSAGE, page, path);
    }

    public static ApiResponse<List<?>> paged(
            HttpStatus status,
            String code,
            String message,
            Page<?> page,
            String path) {
        return new ApiResponse<>(
                Instant.now(),
                true,
                status.value(),
                code,
                message,
                page.getContent(),
                PageMeta.from(page),
                null,
                resolvePath(path),
                currentRequestId());
    }

    public static ApiResponse<Void> error(
            HttpStatus status,
            String code,
            String message,
            Map<String, String> errors,
            String path) {
        return new ApiResponse<>(
                Instant.now(),
                false,
                status.value(),
                code,
                message,
                null,
                null,
                normalizeErrors(errors),
                resolvePath(path),
                currentRequestId());
    }

    private static Map<String, String> normalizeErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) {
            return null;
        }

        Map<String, String> normalized = new LinkedHashMap<>();
        errors.forEach((field, error) -> {
            if (field != null && error != null && !error.isBlank()) {
                normalized.put(field, error);
            }
        });
        return normalized.isEmpty() ? null : Map.copyOf(normalized);
    }

    private static String currentRequestId() {
        return MDC.get(RequestIdFilter.MDC_KEY);
    }

    private static String resolvePath(String path) {
        if (path != null) {
            return path;
        }

        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest().getRequestURI();
        }
        return null;
    }
}
