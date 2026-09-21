package com.S_Health.GenderHealthCare.common.exception;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
import com.S_Health.GenderHealthCare.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.validation.BindException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts expected and unexpected exceptions into the common error envelope.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            BindException exception,
            HttpServletRequest request) {
        Map<String, String> errors = validationErrors(exception.getBindingResult());
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
            MissingPathVariableException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(
            Exception exception,
            HttpServletRequest request) {
        log.debug(CommonMessages.LOG_BAD_REQUEST, request.getRequestURI(), exception.getMessage());
        return errorResponse(ErrorCode.BAD_REQUEST, Map.of(), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.METHOD_NOT_ALLOWED, Map.of(), request);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnsupportedMediaType(
            HttpMediaTypeNotSupportedException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE, Map.of(), request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handlePayloadTooLarge(
            MaxUploadSizeExceededException exception,
            HttpServletRequest request) {
        return errorResponse(ErrorCode.PAYLOAD_TOO_LARGE, Map.of(), request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(
            DomainException exception,
            HttpServletRequest request) {
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            message = exception.getErrorCode().getDefaultMessage();
        }
        if (exception.getCause() != null) {
            log.error(CommonMessages.LOG_DOMAIN_ERROR, request.getRequestURI(), exception);
        }
        return errorResponse(exception.getErrorCode(), exception.getErrors(), request, message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataConflict(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        log.warn(CommonMessages.LOG_DATA_CONFLICT, request.getRequestURI());
        return errorResponse(ErrorCode.CONFLICT, Map.of(), request);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            Exception exception,
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
        log.error(CommonMessages.LOG_UNEXPECTED_ERROR, request.getRequestURI(), exception);
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

    private Map<String, String> validationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        bindingResult.getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        bindingResult.getGlobalErrors().forEach(error ->
                errors.putIfAbsent(error.getObjectName(), error.getDefaultMessage()));
        return errors;
    }
}
