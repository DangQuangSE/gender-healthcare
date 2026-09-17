package com.S_Health.GenderHealthCare.modules.identity;

import com.S_Health.GenderHealthCare.common.exception.DomainException;
import com.S_Health.GenderHealthCare.common.exception.ErrorCode;

public class IdentityException extends DomainException {
    public IdentityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
