package com.freelancemarketplace.backend.response;

public final class ResponseMessage {
    private ResponseMessage() {
    }
    // Success messages (for 2xx status codes)
    public static final String SUCCESS = "Request successful";
    public static final String CREATED = "Resource created successfully";
    public static final String ACCEPTED = "Request accepted for processing";
    public static final String NO_CONTENT = "No content to return";

    // Client error messages (for 4xx status codes)
    public static final String BAD_REQUEST = "Invalid request parameters";
    public static final String UNAUTHORIZED = "Authentication required";
    public static final String FORBIDDEN = "Access denied";
    public static final String NOT_FOUND = "Resource not found";
    public static final String CONFLICT = "Resource conflict occurred";

    // Server error messages (for 5xx status codes)
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred";
    public static final String SERVICE_UNAVAILABLE = "Service temporarily unavailable";

    // Custom messages (for custom status codes)
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String PROJECT_NOT_FOUND = "Project not found";
    public static final String BID_EXPIRED = "Bid has expired";
}
