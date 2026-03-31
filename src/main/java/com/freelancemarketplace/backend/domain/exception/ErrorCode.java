package com.freelancemarketplace.backend.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    EMAIL_ALREADY_EXISTS(1002, "Email already exists"),
    PHONE_NUMBER_ALREADY_EXISTS(1003, "Phone number already exists"),
    EMAIL_OR_PHONE_ALREADY_EXISTS(1004, "Email or phone number already exists"),
    INVALID_CREDENTIALS(1005, "Invalid email or password"),
    EXPIRED_JWT(1006, "JWT token expired"),

    PROJECT_NOT_FOUND(1101, "Project not found"),
    SKILL_ALREADY_EXISTS(1102, "Skill already exists"),
    PROPOSAL_ERROR(1103, "Proposal operation failed"),
    NOTIFICATION_ERROR(1104, "Notification operation failed"),
    LANGUAGE_ERROR(1105, "Language operation failed"),
    QA_ERROR(1106, "Q&A operation failed"),
    EMAIL_SEND_FAILED(1107, "Failed to send email"),
    ADMIN_ERROR(1108, "Admin operation failed"),

    INVALID_REQUEST(9001, "Invalid request"),
    VALIDATION_ERROR(9002, "Validation error"),
    UNAUTHORIZED(9003, "Unauthorized access"),
    FORBIDDEN(9004, "Access denied"),
    INTERNAL_SERVER_ERROR(9000, "An unexpected error occurred");

    private final int code;
    private final String message;
}

