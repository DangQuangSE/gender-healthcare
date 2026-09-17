package com.S_Health.GenderHealthCare.common.exception;

import java.util.Map;

/**
 * Exception for expected API errors raised by application services.
 */
public class ApiException extends DomainException {
    public ApiException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ApiException(ErrorCode errorCode, String message, Map<String, String> errors) {
        super(errorCode, message, errors);
    }

    public ApiException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ApiException(ErrorCode errorCode, String message, Map<String, String> errors, Throwable cause) {
        super(errorCode, message, errors, cause);
    }
}
