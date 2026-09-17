package com.S_Health.GenderHealthCare.exception.exceptions;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

/**
 * Backward-compatible adapter for legacy services.
 */
public class AppException extends DomainException {
    public AppException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
