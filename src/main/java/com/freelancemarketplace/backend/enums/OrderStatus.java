package com.freelancemarketplace.backend.enums;

public enum OrderStatus {
    PENDING,      // The order has been placed but not yet accepted
    ACCEPTED,     // The freelancer has accepted the order
    IN_PROGRESS,  // The work is underway
    UNDER_REVIEW, // The client is reviewing the completed work
    COMPLETED,    // The work is finished and payment has been released
    CANCELLED
}
