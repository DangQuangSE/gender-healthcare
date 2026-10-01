package com.S_Health.GenderHealthCare.common.response;

import com.S_Health.GenderHealthCare.common.message.ApiResponseMessages;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * Wraps normal JSON response bodies from the HTTP API in the common envelope.
 * This keeps versioned and deprecated compatibility endpoints on one response contract.
 */
@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    private static final String API_PREFIX = "/api/";

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (shouldSkip(body, selectedContentType) || !isApiPath(path)) {
            return body;
        }

        HttpStatus status = resolveStatus(response);

        if (body instanceof Page<?> page) {
            return ApiResponse.paged(status, ApiResponseMessages.SUCCESS_CODE, ApiResponseMessages.SUCCESS_MESSAGE, page, path);
        }

        return ApiResponse.success(status, ApiResponseMessages.SUCCESS_CODE, ApiResponseMessages.SUCCESS_MESSAGE, body, path);
    }

    private boolean isApiPath(String path) {
        return path != null && path.startsWith(API_PREFIX);
    }

    private boolean shouldSkip(Object body, MediaType contentType) {
        return body == null
                || body instanceof ApiResponse<?>
                || body instanceof Resource
                || body instanceof byte[]
                || body instanceof StreamingResponseBody
                || body instanceof String
                || contentType == null
                || !MediaType.APPLICATION_JSON.isCompatibleWith(contentType);
    }

    private HttpStatus resolveStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            int status = servletResponse.getServletResponse().getStatus();
            if (status >= 100 && status <= 599) {
                return HttpStatus.valueOf(status);
            }
        }
        return HttpStatus.OK;
    }
}
