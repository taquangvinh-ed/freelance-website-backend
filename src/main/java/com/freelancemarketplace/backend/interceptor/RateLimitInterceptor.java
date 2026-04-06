/**
 * Rate Limit Response Interceptor
 * Thêm header chuẩn: X-RateLimit-Limit, X-RateLimit-Remaining, X-RateLimit-Reset
 */

package com.freelancemarketplace.backend.interceptor;

import com.freelancemarketplace.backend.util.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int REQUESTS_PER_HOUR = 10;
    private static final long WINDOW_SECONDS = 60 * 60; // 1 hour

    private static final RateLimiter rateLimiter = new RateLimiter(REQUESTS_PER_HOUR, WINDOW_SECONDS);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Chỉ áp dụng rate limit cho AI endpoints
        String path = request.getRequestURI();
        if (!path.contains("/api/ai/")) {
            return true;
        }

        // Get user ID from request (nếu có)
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            userId = 0L; // Anonymous user
        }

        // Check rate limit
        boolean allowed = rateLimiter.allowRequest(userId);

        int remaining = rateLimiter.getRemainingRequests(userId);
        long resetTime = rateLimiter.getResetTime(userId);

        // Add rate limit headers
        response.setHeader("X-RateLimit-Limit", String.valueOf(REQUESTS_PER_HOUR));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, remaining)));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetTime));

        if (!allowed) {
            log.warn("Rate limit exceeded for user {}", userId);
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json");
            try {
                response.getWriter().write("{\"error\":\"Rate limit exceeded\"}");
            } catch (Exception e) {
                log.error("Error writing rate limit response", e);
            }
            return false;
        }

        return true;
    }

    /**
     * Extract user ID from request (from JWT token or header)
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        try {
            // Try to get from custom header (if frontend sends it)
            String userId = request.getHeader("X-User-Id");
            if (userId != null) {
                return Long.parseLong(userId);
            }
            // Could also extract from JWT token if needed
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}


