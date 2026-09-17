package com.S_Health.GenderHealthCare.common.exception;

import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts expected and unexpected exceptions into the common error envelope.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return errorResponse(ErrorCode.VALIDATION_ERROR, errors, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                errors.putIfAbsent(violation.getPropertyPath().toString(), violation.getMessage()));
        return errorResponse(ErrorCode.VALIDATION_ERROR, errors, request);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            Exception exception,
            HttpServletRequest request) {
        log.debug("Bad request at {}: {}", request.getRequestURI(), exception.getMessage());
        return errorResponse(ErrorCode.BAD_REQUEST, Map.of(), request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(
            DomainException exception,
            HttpServletRequest request) {
        return errorResponse(exception.getErrorCode(), exception.getErrors(), request, exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataConflict(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        log.warn("Data conflict at {}", request.getRequestURI());
        return errorResponse(ErrorCode.CONFLICT, Map.of(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            NoResourceFoundException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.NOT_FOUND, Map.of(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(
            AuthenticationException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.UNAUTHENTICATED, Map.of(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.FORBIDDEN, Map.of(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        log.error("Unexpected error at {}", request.getRequestURI(), exception);
        return errorResponse(ErrorCode.INTERNAL_ERROR, Map.of(), request);
    }

    private ResponseEntity<ApiResponse<Void>> errorResponse(
            ErrorCode errorCode,
            Map<String, String> errors,
            HttpServletRequest request) {
        return errorResponse(errorCode, errors, request, errorCode.getDefaultMessage());
    }

    private ResponseEntity<ApiResponse<Void>> errorResponse(
            ErrorCode errorCode,
            Map<String, String> errors,
            HttpServletRequest request,
            String message) {
        ApiResponse<Void> response = ApiResponse.error(
                errorCode.getStatus(),
                errorCode.name(),
                message,
                errors.isEmpty() ? null : errors,
                request.getRequestURI());
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
