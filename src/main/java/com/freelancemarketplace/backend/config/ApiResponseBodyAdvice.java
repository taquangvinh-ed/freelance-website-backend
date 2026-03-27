package com.freelancemarketplace.backend.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelancemarketplace.backend.dto.ApiResponse;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Objects;

@RestControllerAdvice(basePackages = "com.freelancemarketplace.backend.controller")
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final String DEFAULT_SUCCESS_MESSAGE = "Request successful";

    private final ObjectMapper objectMapper;

    public ApiResponseBodyAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        String path = request.getURI().getPath();

        // Stripe webhook expects raw response body; wrapping will break signature flow.
        if ("/api/stripe/webhook".equals(path)) {
            return body;
        }

        if (body instanceof ApiResponse<?>) {
            return body;
        }

        if (body == null) {
            HttpStatus status = resolveHttpStatus(response);
            if (status == HttpStatus.NO_CONTENT) {
                return null;
            }
            return ApiResponse.success(status.value(), DEFAULT_SUCCESS_MESSAGE, null);
        }

        if (body instanceof Resource || body instanceof byte[] || body instanceof StreamingResponseBody) {
            return body;
        }

        ApiResponse<?> wrapped;
        if (body instanceof ResponseDTO<?> legacy) {
            int code = parseLegacyCode(legacy.getStatusCode(), resolveHttpStatus(response).value());
            boolean success = code >= 200 && code < 300;
            wrapped = success
                    ? ApiResponse.success(code, legacy.getStatusMessage(), legacy.getBody())
                    : ApiResponse.error(code, legacy.getStatusMessage(), legacy.getBody());
        } else {
            HttpStatus status = resolveHttpStatus(response);
            wrapped = ApiResponse.success(status.value(), DEFAULT_SUCCESS_MESSAGE, body);
        }

        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                return objectMapper.writeValueAsString(wrapped);
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("Unable to serialize ApiResponse", ex);
            }
        }

        return wrapped;
    }

    private HttpStatus resolveHttpStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            int code = servletResponse.getServletResponse().getStatus();
            return HttpStatus.resolve(code) == null ? HttpStatus.OK : Objects.requireNonNull(HttpStatus.resolve(code));
        }
        return HttpStatus.OK;
    }

    private int parseLegacyCode(String statusCode, int fallback) {
        try {
            return Integer.parseInt(statusCode);
        } catch (Exception ex) {
            return fallback;
        }
    }
}

