/**
 * Unified Response Wrapper for All Endpoints
 * Chuẩn hóa contract: Tất cả endpoints dùng chung 1 format
 */

package com.freelancemarketplace.backend.toggl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnifiedApiResponse<T> {

    // ========== RESPONSE ENVELOPE ==========
    private boolean success;
    private String message;
    private String timestamp;

    // ========== ERROR DETAILS ==========
    private String errorCode;  // e.g., RATE_LIMIT_EXCEEDED, AI_TIMEOUT
    private boolean retryable; // Có thể thử lại không?

    // ========== ACTUAL DATA ==========
    private T data;

    // ========== METADATA ==========
    private Map<String, Object> metadata; // Để trả thêm info (pagination, tracing, etc.)

    // ========== CONSTRUCTORS ==========

    /**
     * Success response
     */
    public UnifiedApiResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }

    /**
     * Error response
     */
    public UnifiedApiResponse(String message, String errorCode, boolean retryable) {
        this.success = false;
        this.message = message;
        this.errorCode = errorCode;
        this.retryable = retryable;
        this.timestamp = Instant.now().toString();
    }

    /**
     * Success with metadata
     */
    public static <T> UnifiedApiResponse<T> success(String message, T data, Map<String, Object> metadata) {
        UnifiedApiResponse<T> response = new UnifiedApiResponse<>(message, data);
        response.setMetadata(metadata);
        return response;
    }

    /**
     * Error with metadata
     */
    public static <T> UnifiedApiResponse<T> error(String message, String errorCode, boolean retryable, Map<String, Object> metadata) {
        UnifiedApiResponse<T> response = new UnifiedApiResponse<>(message, errorCode, retryable);
        response.setMetadata(metadata);
        return response;
    }

    /**
     * Common error codes
     */
    public enum ErrorCode {
        RATE_LIMIT_EXCEEDED,      // Rate limit vượt quá
        AI_TIMEOUT,               // AI request timeout
        INVALID_INPUT,            // Input validation failed
        INVALID_TOKEN,            // JWT token invalid/expired
        UNAUTHORIZED,             // User not authenticated
        NOT_FOUND,                // Resource not found
        INTERNAL_ERROR,           // Server error
        AI_ERROR                  // AI generation failed
    }
}

