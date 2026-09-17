package com.S_Health.GenderHealthCare.common.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Base exception for errors that can be safely returned to the client.
 */
public class DomainException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, String> errors;

    public DomainException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), Collections.emptyMap());
    }

    public DomainException(ErrorCode errorCode, String message) {
        this(errorCode, message, Collections.emptyMap());
    }

    public DomainException(ErrorCode errorCode, String message, Map<String, String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors == null ? Collections.emptyMap() : Map.copyOf(errors);
    }

    public DomainException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errors = Collections.emptyMap();
    }

    public DomainException(
            ErrorCode errorCode,
            String message,
            Map<String, String> errors,
            Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errors = errors == null ? Collections.emptyMap() : Map.copyOf(errors);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
