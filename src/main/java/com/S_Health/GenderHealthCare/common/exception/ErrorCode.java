package com.S_Health.GenderHealthCare.common.exception;

import com.S_Health.GenderHealthCare.common.message.ApiResponseMessages;
import org.springframework.http.HttpStatus;

/**
 * Error codes shared by all backend modules.
 */
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, ApiResponseMessages.BAD_REQUEST),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, ApiResponseMessages.VALIDATION_FAILED),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, ApiResponseMessages.AUTHENTICATION_REQUIRED),
    FORBIDDEN(HttpStatus.FORBIDDEN, ApiResponseMessages.ACCESS_DENIED),
    NOT_FOUND(HttpStatus.NOT_FOUND, ApiResponseMessages.RESOURCE_NOT_FOUND),
    CONFLICT(HttpStatus.CONFLICT, ApiResponseMessages.RESOURCE_CONFLICT),
    INTEGRATION_ERROR(HttpStatus.BAD_GATEWAY, ApiResponseMessages.INTEGRATION_FAILED),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, ApiResponseMessages.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ApiResponseMessages.MEDIA_TYPE_NOT_SUPPORTED),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, ApiResponseMessages.PAYLOAD_TOO_LARGE),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponseMessages.INTERNAL_ERROR);

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
