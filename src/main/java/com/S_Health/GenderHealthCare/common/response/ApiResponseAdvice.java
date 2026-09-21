package com.S_Health.GenderHealthCare.common.response;

import com.S_Health.GenderHealthCare.common.message.CommonMessages;
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
 * Wraps normal JSON response bodies in the common response envelope.
 */
@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

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
        if (shouldSkip(body, selectedContentType) || !isVersionedApi(path)) {
            return body;
        }

        HttpStatus status = resolveStatus(response);

        if (body instanceof Page<?> page) {
            return ApiResponse.paged(status, CommonMessages.REQUEST_SUCCESS_CODE, CommonMessages.REQUEST_COMPLETED, page, path);
        }

        return ApiResponse.success(status, CommonMessages.REQUEST_SUCCESS_CODE, CommonMessages.REQUEST_COMPLETED, body, path);
    }

    private boolean isVersionedApi(String path) {
        return path != null && path.startsWith("/api/v1/");
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
            return HttpStatus.valueOf(servletResponse.getServletResponse().getStatus());
        }
        return HttpStatus.OK;
    }
}
