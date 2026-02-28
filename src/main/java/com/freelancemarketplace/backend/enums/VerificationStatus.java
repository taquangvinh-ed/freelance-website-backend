package com.freelancemarketplace.backend.enums;

public enum VerificationStatus {
    PENDING,           // User submitted ID card, waiting for OTP
    OTP_VERIFIED,      // OTP verified, waiting for admin approval
    APPROVED,          // Admin approved
    REJECTED           // Admin rejected
}

