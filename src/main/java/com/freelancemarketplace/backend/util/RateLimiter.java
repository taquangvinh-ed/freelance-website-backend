package com.freelancemarketplace.backend.util;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Simple Rate Limiter - Token Bucket Algorithm
 * No external dependencies needed
 */
@Slf4j
public class RateLimiter {

    private final int maxRequests;
    private final long windowSeconds;
    private final ConcurrentHashMap<Long, ConcurrentLinkedQueue<Long>> requestTimestamps;

    /**
     * Constructor
     * @param maxRequests Max requests allowed
     * @param windowSeconds Time window in seconds
     */
    public RateLimiter(int maxRequests, long windowSeconds) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
        this.requestTimestamps = new ConcurrentHashMap<>();
    }

    /**
     * Check if request is allowed
     * @param userId User ID
     * @return true if allowed, false if rate limited
     */
    public synchronized boolean allowRequest(Long userId) {
        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000);

        // Get or create queue for this user
        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());

        // Remove old timestamps outside window
        timestamps.removeIf(timestamp -> timestamp < windowStart);

        // Check if limit exceeded
        if (timestamps.size() >= maxRequests) {
            log.warn("Rate limit exceeded for user {}", userId);
            return false;
        }

        // Add current request
        timestamps.offer(now);
        return true;
    }

    /**
     * Get remaining requests for user
     * @param userId User ID
     * @return Number of remaining requests
     */
    public int getRemainingRequests(Long userId) {
        long now = System.currentTimeMillis();
        long windowStart = now - (windowSeconds * 1000);

        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.get(userId);
        if (timestamps == null) {
            return maxRequests;
        }

        // Count valid requests (within window)
        int validRequests = (int) timestamps.stream()
                .filter(timestamp -> timestamp >= windowStart)
                .count();

        return Math.max(0, maxRequests - validRequests);
    }

    /**
     * Get reset time (when limit resets)
     * @param userId User ID
     * @return Unix timestamp in seconds when rate limit resets
     */
    public long getResetTime(Long userId) {
        ConcurrentLinkedQueue<Long> timestamps = requestTimestamps.get(userId);
        if (timestamps == null || timestamps.isEmpty()) {
            return System.currentTimeMillis() / 1000 + windowSeconds;
        }

        // Get oldest request
        Long oldestTimestamp = timestamps.peek();
        if (oldestTimestamp == null) {
            return System.currentTimeMillis() / 1000 + windowSeconds;
        }

        return (oldestTimestamp / 1000) + windowSeconds;
    }

    /**
     * Reset all rate limits (for testing)
     */
    public void reset() {
        requestTimestamps.clear();
    }
}

