package com.freelancemarketplace.backend.response;

public final class ResponseStatusCode {
    private ResponseStatusCode() {
    }
    // Success status codes (2xx)
    public static final String SUCCESS = "200";
    public static final String CREATED = "201";
    public static final String ACCEPTED = "202";
    public static final String NO_CONTENT = "204";

    // Client error status codes (4xx)
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";
    public static final String CONFLICT = "409";

    // Server error status codes (5xx)
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String SERVICE_UNAVAILABLE = "503";

    // Custom status codes (if needed for your freelance platform)
    public static final String USER_ALREADY_EXISTS = "1001";
    public static final String INVALID_CREDENTIALS = "1002";
    public static final String PROJECT_NOT_FOUND = "1003";
    public static final String BID_EXPIRED = "1004";
}
