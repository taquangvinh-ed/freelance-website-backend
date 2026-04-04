package com.freelancemarketplace.backend.exceptionHandling;

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
    INVALID_OPERATION(9005, "Invalid operation"),
    CONFIGURATION_ERROR(9006, "Service configuration error"),
    EXTERNAL_SERVICE_ERROR(9007, "External service call failed"),
    RATE_LIMIT_EXCEEDED(9008, "Rate limit exceeded"),
    INTERNAL_SERVER_ERROR(9000, "An unexpected error occurred"),

    ADMIN_NOT_FOUND(1201, "Admin not found"),
    NOTIFICATION_NOT_FOUND(1202, "Notification not found"),
    CLIENT_NOT_FOUND(1203, "Client not found"),
    FREELANCER_NOT_FOUND(1204, "Freelancer not found"),
    CONTRACT_NOT_FOUND(1205, "Contract not found"),
    MILESTONE_NOT_FOUND(1206, "Milestone not found"),
    PAYMENT_NOT_FOUND(1207, "Payment not found"),
    PRODUCT_NOT_FOUND(1208, "Product not found"),
    LOCATION_NOT_FOUND(1209, "Location not found"),
    SKILL_NOT_FOUND(1210, "Skill not found"),
    PROJECT_QUESTION_NOT_FOUND(1211, "Project question not found"),
    BUDGET_NOT_FOUND(1212, "Budget not found"),
    VERIFICATION_NOT_FOUND(1213, "Verification not found"),
    INVALID_OTP(1214, "Invalid OTP"),
    REVIEW_OPERATION_NOT_ALLOWED(1215, "Review operation not allowed");

    private final int code;
    private final String message;
}

