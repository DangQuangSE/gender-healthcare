package com.S_Health.GenderHealthCare.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Error codes shared by all backend modules.
 */
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Request is invalid"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Request validation failed"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Authentication is required"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "You do not have permission to perform this action"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "The requested resource was not found"),
    CONFLICT(HttpStatus.CONFLICT, "The request conflicts with the current resource state"),
    INTEGRATION_ERROR(HttpStatus.BAD_GATEWAY, "External service request failed"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
