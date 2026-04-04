package com.freelancemarketplace.backend.user.api.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.user.dto.AccountVerificationDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.user.api.request.ReviewVerificationRequest;
import com.freelancemarketplace.backend.user.api.request.VerifyOtpRequest;
import com.freelancemarketplace.backend.user.application.service.AccountVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
@Slf4j
public class AccountVerificationController {

    private final AccountVerificationService verificationService;

    /**
     * Step 1: User submits Citizen ID Card photo
     * POST /api/verification/submit-id-card
     */
    @PostMapping(value = "/submit-id-card", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> submitCitizenIdCard(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam("idCard") MultipartFile idCardFile) {

        try {
            log.info("User {} submitting ID card", appUser.getId());

            if (idCardFile.isEmpty()) {
                return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), "ID card file is required", null);
            }

            AccountVerificationDTO result = verificationService.submitCitizenIdCard(
                    appUser.getId(),
                    idCardFile
            );

            return ApiResponse.success(2000, "ID card submitted successfully. OTP sent to your email.", result);

        } catch (IOException e) {
            log.error("Error uploading ID card: {}", e.getMessage());
            return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                    "Failed to upload ID card: " + e.getMessage(), null);
        } catch (IllegalStateException e) {
            return ApiResponse.error(ErrorCode.INVALID_OPERATION.getCode(), e.getMessage(), null);
        }
    }

    /**
     * Step 2: Verify OTP code
     * POST /api/verification/verify-otp
     */
    @PostMapping("/verify-otp")
    public ApiResponse<?> verifyOtp(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody VerifyOtpRequest request) {

        try {
            log.info("User {} verifying OTP", appUser.getId());

            AccountVerificationDTO result = verificationService.verifyOtp(
                    appUser.getId(),
                    request.getOtpCode()
            );

            return ApiResponse.success(2000, "OTP verified successfully. Waiting for admin approval.", result);

        } catch (IllegalArgumentException e) {
            return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), e.getMessage(), null);
        } catch (IllegalStateException e) {
            return ApiResponse.error(ErrorCode.INVALID_OPERATION.getCode(), e.getMessage(), null);
        }
    }

    /**
     * Resend OTP code
     * POST /api/verification/resend-otp
     */
    @PostMapping("/resend-otp")
    public ApiResponse<?> resendOtp(@AuthenticationPrincipal AppUser appUser) {
        try {
            log.info("User {} requesting OTP resend", appUser.getId());

            verificationService.resendOtp(appUser.getId());

            return ApiResponse.success(2000, "OTP resent successfully to your email", null);

        } catch (IllegalStateException e) {
            return ApiResponse.error(ErrorCode.INVALID_OPERATION.getCode(), e.getMessage(), null);
        }
    }

    /**
     * Get verification status for current user
     * GET /api/verification/status
     */
    @GetMapping("/status")
    public ApiResponse<?> getVerificationStatus(@AuthenticationPrincipal AppUser appUser) {
        log.info("User {} checking verification status", appUser.getId());

        AccountVerificationDTO status = verificationService.getVerificationStatus(appUser.getId());

        if (status == null) {
            return ApiResponse.success(2000, "No verification request found", null);
        }

        return ApiResponse.success(status);
    }

    /**
     * Admin: Get all pending verifications (OTP_VERIFIED status)
     * GET /api/verification/admin/pending
     */
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getPendingVerifications() {
        log.info("Admin fetching pending verifications");

        List<AccountVerificationDTO> verifications = verificationService.getPendingVerifications();

        return ApiResponse.success(verifications);
    }

    /**
     * Admin: Get verifications by status
     * GET /api/verification/admin/by-status/{status}
     */
    @GetMapping("/admin/by-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> getVerificationsByStatus(@PathVariable String status) {
        log.info("Admin fetching verifications with status: {}", status);

        try {
            List<AccountVerificationDTO> verifications =
                    verificationService.getVerificationsByStatus(status);

            return ApiResponse.success(verifications);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), "Invalid status: " + status, null);
        }
    }

    /**
     * Admin: Review and approve/reject verification
     * PUT /api/verification/admin/review/{verificationId}
     */
    @PutMapping("/admin/review/{verificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> reviewVerification(
            @PathVariable Long verificationId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody ReviewVerificationRequest request) {

        try {
            log.info("Admin {} reviewing verification {}", appUser.getId(), verificationId);

            if (request.getApproved() == null) {
                return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(), "Approval decision is required", null);
            }

            if (!request.getApproved() &&
                (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty())) {
                return ApiResponse.error(ErrorCode.INVALID_REQUEST.getCode(),
                        "Rejection reason is required when rejecting verification", null);
            }

            AccountVerificationDTO result = verificationService.reviewVerification(
                    verificationId,
                    appUser.getId(),
                    request
            );

            String message = request.getApproved()
                    ? "Verification approved successfully"
                    : "Verification rejected successfully";

            return ApiResponse.success(2000, message, result);

        } catch (IllegalStateException e) {
            return ApiResponse.error(ErrorCode.INVALID_OPERATION.getCode(), e.getMessage(), null);
        }
    }
}

