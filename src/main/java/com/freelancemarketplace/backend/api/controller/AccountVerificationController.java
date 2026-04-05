package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.user.dto.AccountVerificationDTO;
import com.freelancemarketplace.backend.api.response.ResponseDTO;
import com.freelancemarketplace.backend.user.api.request.ReviewVerificationRequest;
import com.freelancemarketplace.backend.api.request.VerifyOtpRequest;
import com.freelancemarketplace.backend.api.response.ResponseMessage;
import com.freelancemarketplace.backend.api.response.ResponseStatusCode;
import com.freelancemarketplace.backend.user.application.service.AccountVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO<?>> submitCitizenIdCard(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam("idCard") MultipartFile idCardFile) {

        try {
            log.info("User {} submitting ID card", appUser.getId());

            if (idCardFile.isEmpty()) {
                return badRequest("ID card file is required");
            }

            AccountVerificationDTO result = verificationService.submitCitizenIdCard(
                    appUser.getId(),
                    idCardFile
            );

            return ok("ID card submitted successfully. OTP sent to your email.", result);

        } catch (IOException e) {
            log.error("Error uploading ID card: {}", e.getMessage());
            return serverError("Failed to upload ID card: " + e.getMessage());
        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Step 2: Verify OTP code
     * POST /api/verification/verify-otp
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDTO<?>> verifyOtp(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody VerifyOtpRequest request) {

        try {
            log.info("User {} verifying OTP", appUser.getId());

            AccountVerificationDTO result = verificationService.verifyOtp(
                    appUser.getId(),
                    request.getOtpCode()
            );

            return ok("OTP verified successfully. Waiting for admin approval.", result);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Resend OTP code
     * POST /api/verification/resend-otp
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseDTO<?>> resendOtp(@AuthenticationPrincipal AppUser appUser) {
        try {
            log.info("User {} requesting OTP resend", appUser.getId());

            verificationService.resendOtp(appUser.getId());

            return ok("OTP resent successfully to your email", null);

        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    /**
     * Get verification status for current user
     * GET /api/verification/status
     */
    @GetMapping("/status")
    public ResponseEntity<ResponseDTO<?>> getVerificationStatus(@AuthenticationPrincipal AppUser appUser) {
        log.info("User {} checking verification status", appUser.getId());

        AccountVerificationDTO status = verificationService.getVerificationStatus(appUser.getId());

        if (status == null) {
            return ok("No verification request found", null);
        }

        return ok(ResponseMessage.SUCCESS, status);
    }

    /**
     * Admin: Get all pending verifications (OTP_VERIFIED status)
     * GET /api/verification/admin/pending
     */
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<?>> getPendingVerifications() {
        log.info("Admin fetching pending verifications");

        List<AccountVerificationDTO> verifications = verificationService.getPendingVerifications();

        return ok(ResponseMessage.SUCCESS, verifications);
    }

    /**
     * Admin: Get verifications by status
     * GET /api/verification/admin/by-status/{status}
     */
    @GetMapping("/admin/by-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<?>> getVerificationsByStatus(@PathVariable String status) {
        log.info("Admin fetching verifications with status: {}", status);

        try {
            List<AccountVerificationDTO> verifications =
                    verificationService.getVerificationsByStatus(status);

            return ok(ResponseMessage.SUCCESS, verifications);
        } catch (IllegalArgumentException e) {
            return badRequest("Invalid status: " + status);
        }
    }

    /**
     * Admin: Review and approve/reject verification
     * PUT /api/verification/admin/review/{verificationId}
     */
    @PutMapping("/admin/review/{verificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<?>> reviewVerification(
            @PathVariable Long verificationId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody ReviewVerificationRequest request) {

        try {
            log.info("Admin {} reviewing verification {}", appUser.getId(), verificationId);

            if (request.getApproved() == null) {
                return badRequest("Approval decision is required");
            }

            if (!request.getApproved() &&
                (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty())) {
                return badRequest("Rejection reason is required when rejecting verification");
            }

            AccountVerificationDTO result = verificationService.reviewVerification(
                    verificationId,
                    appUser.getId(),
                    request
            );

            String message = request.getApproved()
                    ? "Verification approved successfully"
                    : "Verification rejected successfully";

            return ok(message, result);

        } catch (IllegalStateException e) {
            return badRequest(e.getMessage());
        }
    }

    private ResponseEntity<ResponseDTO<?>> ok(String message, Object body) {
        return build(HttpStatus.OK, ResponseStatusCode.SUCCESS, message, body);
    }

    private ResponseEntity<ResponseDTO<?>> badRequest(String message) {
        return build(HttpStatus.BAD_REQUEST, ResponseStatusCode.BAD_REQUEST, message, null);
    }

    private ResponseEntity<ResponseDTO<?>> serverError(String message) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusCode.INTERNAL_SERVER_ERROR, message, null);
    }

    private ResponseEntity<ResponseDTO<?>> build(HttpStatus status, String statusCode, String message, Object body) {
        return ResponseEntity
                .status(status)
                .body(new ResponseDTO<>(statusCode, message, body));
    }
}
