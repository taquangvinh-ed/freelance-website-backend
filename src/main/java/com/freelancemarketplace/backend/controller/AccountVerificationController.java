package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.AccountVerificationDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.request.ReviewVerificationRequest;
import com.freelancemarketplace.backend.request.VerifyOtpRequest;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.AccountVerificationService;
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
    public ResponseEntity<ResponseDTO> submitCitizenIdCard(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam("idCard") MultipartFile idCardFile) {

        try {
            log.info("User {} submitting ID card", appUser.getId());

            if (idCardFile.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(
                                ResponseStatusCode.BAD_REQUEST,
                                "ID card file is required",
                                null
                        ));
            }

            AccountVerificationDTO result = verificationService.submitCitizenIdCard(
                    appUser.getId(),
                    idCardFile
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            "ID card submitted successfully. OTP sent to your email.",
                            result
                    ));

        } catch (IOException e) {
            log.error("Error uploading ID card: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(
                            ResponseStatusCode.INTERNAL_SERVER_ERROR,
                            "Failed to upload ID card: " + e.getMessage(),
                            null
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            e.getMessage(),
                            null
                    ));
        }
    }

    /**
     * Step 2: Verify OTP code
     * POST /api/verification/verify-otp
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDTO> verifyOtp(
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody VerifyOtpRequest request) {

        try {
            log.info("User {} verifying OTP", appUser.getId());

            AccountVerificationDTO result = verificationService.verifyOtp(
                    appUser.getId(),
                    request.getOtpCode()
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            "OTP verified successfully. Waiting for admin approval.",
                            result
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            e.getMessage(),
                            null
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            e.getMessage(),
                            null
                    ));
        }
    }

    /**
     * Resend OTP code
     * POST /api/verification/resend-otp
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<ResponseDTO> resendOtp(@AuthenticationPrincipal AppUser appUser) {
        try {
            log.info("User {} requesting OTP resend", appUser.getId());

            verificationService.resendOtp(appUser.getId());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            "OTP resent successfully to your email",
                            null
                    ));

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            e.getMessage(),
                            null
                    ));
        }
    }

    /**
     * Get verification status for current user
     * GET /api/verification/status
     */
    @GetMapping("/status")
    public ResponseEntity<ResponseDTO> getVerificationStatus(@AuthenticationPrincipal AppUser appUser) {
        log.info("User {} checking verification status", appUser.getId());

        AccountVerificationDTO status = verificationService.getVerificationStatus(appUser.getId());

        if (status == null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            "No verification request found",
                            null
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        status
                ));
    }

    /**
     * Admin: Get all pending verifications (OTP_VERIFIED status)
     * GET /api/verification/admin/pending
     */
    @GetMapping("/admin/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getPendingVerifications() {
        log.info("Admin fetching pending verifications");

        List<AccountVerificationDTO> verifications = verificationService.getPendingVerifications();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        verifications
                ));
    }

    /**
     * Admin: Get verifications by status
     * GET /api/verification/admin/by-status/{status}
     */
    @GetMapping("/admin/by-status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getVerificationsByStatus(@PathVariable String status) {
        log.info("Admin fetching verifications with status: {}", status);

        try {
            List<AccountVerificationDTO> verifications =
                    verificationService.getVerificationsByStatus(status);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            ResponseMessage.SUCCESS,
                            verifications
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            "Invalid status: " + status,
                            null
                    ));
        }
    }

    /**
     * Admin: Review and approve/reject verification
     * PUT /api/verification/admin/review/{verificationId}
     */
    @PutMapping("/admin/review/{verificationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> reviewVerification(
            @PathVariable Long verificationId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestBody ReviewVerificationRequest request) {

        try {
            log.info("Admin {} reviewing verification {}", appUser.getId(), verificationId);

            if (request.getApproved() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(
                                ResponseStatusCode.BAD_REQUEST,
                                "Approval decision is required",
                                null
                        ));
            }

            if (!request.getApproved() &&
                (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(
                                ResponseStatusCode.BAD_REQUEST,
                                "Rejection reason is required when rejecting verification",
                                null
                        ));
            }

            AccountVerificationDTO result = verificationService.reviewVerification(
                    verificationId,
                    appUser.getId(),
                    request
            );

            String message = request.getApproved()
                    ? "Verification approved successfully"
                    : "Verification rejected successfully";

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            ResponseStatusCode.SUCCESS,
                            message,
                            result
                    ));

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(
                            ResponseStatusCode.BAD_REQUEST,
                            e.getMessage(),
                            null
                    ));
        }
    }
}

