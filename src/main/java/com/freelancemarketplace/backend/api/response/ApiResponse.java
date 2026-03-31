package com.freelancemarketplace.backend.api.response;

import com.freelancemarketplace.backend.service.FreelancerService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse <T>{
    private boolean success;
    private String code;
    private String message;
    private T data;


    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(ResponseStatusCode.CREATED)
                .message(ResponseMessage.CREATED)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(ResponseStatusCode.SUCCESS)
                .message(ResponseMessage.SUCCESS)
                .data(data)
                .build();
    }


    public static <T> ApiResponse<T> delete() {
        return ApiResponse.<T>builder()
                .success(false)
                .code(ResponseStatusCode.NO_CONTENT)
                .message(ResponseMessage.NO_CONTENT)
                .build();
    }

    public static <T> ApiResponse<T> bad_request(T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(ResponseStatusCode.BAD_REQUEST)
                .message(ResponseMessage.BAD_REQUEST)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(ResponseStatusCode.INTERNAL_SERVER_ERROR)
                .message(ResponseMessage.INTERNAL_SERVER_ERROR)
                .build();
    }

    public static ApiResponse<?> error(int code, String message, Object o) {
        return ApiResponse.builder()
                .success(false)
                .code(String.valueOf(code))
                .message(message)
                .data(o)
                .build();
    }
}
