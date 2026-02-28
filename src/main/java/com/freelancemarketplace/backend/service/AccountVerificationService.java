package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.AccountVerificationDTO;
import com.freelancemarketplace.backend.request.ReviewVerificationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AccountVerificationService {

    /**
     * Step 1: User submits Citizen ID Card photo
     */
    AccountVerificationDTO submitCitizenIdCard(Long userId, MultipartFile idCardFile) throws IOException;

    /**
     * Step 2: Verify OTP code
     */
    AccountVerificationDTO verifyOtp(Long userId, String otpCode);

    /**
     * Resend OTP code to user's email
     */
    void resendOtp(Long userId);

    /**
     * Admin: Get all pending verifications
     */
    List<AccountVerificationDTO> getPendingVerifications();

    /**
     * Admin: Get all verifications with specific status
     */
    List<AccountVerificationDTO> getVerificationsByStatus(String status);

    /**
     * Admin: Review and approve/reject verification
     */
    AccountVerificationDTO reviewVerification(Long verificationId, Long adminId, ReviewVerificationRequest request);

    /**
     * Get verification status for a user
     */
    AccountVerificationDTO getVerificationStatus(Long userId);
}

