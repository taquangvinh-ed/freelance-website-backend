package com.freelancemarketplace.backend.common.api.response;

import com.freelancemarketplace.backend.exceptionHandling.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private Timestamp timestamp;
    private T body;

    public static <T> ApiResponse<T> created(T body) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(SuccessCode.CREATE_SUCCESS.getCode())
                .message(SuccessCode.CREATE_SUCCESS.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .body(body)
                .build();
    }
    public static <T> ApiResponse<T> success(T body) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(SuccessCode.SUCCESS.getCode())
                .message(SuccessCode.SUCCESS.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .body(body)
                .build();
    }

    public static <T> ApiResponse<T> success(int code, String message, T body) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(code)
                .message(message)
                .timestamp(Timestamp.from(Instant.now()))
                .body(body)
                .build();
    }

    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder()
                .success(true)
                .code(SuccessCode.NO_CONTENT.getCode())
                .message(SuccessCode.NO_CONTENT.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message, T body) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(Timestamp.from(Instant.now()))
                .body(body)
                .build();
    }
}

