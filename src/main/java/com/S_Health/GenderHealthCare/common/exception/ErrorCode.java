package com.S_Health.GenderHealthCare.common.exception;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import org.springframework.http.HttpStatus;

/**
 * Error codes shared by all backend modules.
 */
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, CommonMessages.ERROR_BAD_REQUEST),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, CommonMessages.ERROR_VALIDATION_FAILED),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, CommonMessages.ERROR_AUTHENTICATION_REQUIRED),
    FORBIDDEN(HttpStatus.FORBIDDEN, CommonMessages.ERROR_ACCESS_DENIED),
    NOT_FOUND(HttpStatus.NOT_FOUND, CommonMessages.ERROR_RESOURCE_NOT_FOUND),
    CONFLICT(HttpStatus.CONFLICT, CommonMessages.ERROR_RESOURCE_CONFLICT),
    INTEGRATION_ERROR(HttpStatus.BAD_GATEWAY, CommonMessages.ERROR_INTEGRATION_FAILED),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, CommonMessages.ERROR_METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, CommonMessages.ERROR_MEDIA_TYPE_NOT_SUPPORTED),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, CommonMessages.ERROR_PAYLOAD_TOO_LARGE),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, CommonMessages.ERROR_INTERNAL);

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
