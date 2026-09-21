package com.S_Health.GenderHealthCare.common.exception;

/**
 * A single validation error that can be mapped to the common errors object.
 */
public record ValidationError(String field, String message) {
}
