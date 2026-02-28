package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.AccountVerificationDTO;
import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.enums.VerificationStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.AccountVerificationModel;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.AccountVerificationRepository;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.request.ReviewVerificationRequest;
import com.freelancemarketplace.backend.service.AccountVerificationService;
import com.freelancemarketplace.backend.service.CloudinaryService;
import com.freelancemarketplace.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountVerificationServiceImp implements AccountVerificationService {

    private final AccountVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final AdminsRepository adminsRepository;
    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;

    @Override
    @Transactional
    public AccountVerificationDTO submitCitizenIdCard(Long userId, MultipartFile idCardFile) throws IOException {
        log.info("User {} submitting citizen ID card for verification", userId);

        // Find user
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Validate email exists
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            log.error("User {} has no email address", userId);
            throw new IllegalStateException("User email is not set. Please update your email address first.");
        }

        log.debug("User found: {} with email: {}", user.getUsername(), user.getEmail());

        // Check if verification already exists
        AccountVerificationModel verification = verificationRepository.findByUser_UserId(userId)
                .orElse(new AccountVerificationModel());

        // If verification exists and already approved, reject new submission
        if (verification.getVerificationId() != null &&
            verification.getStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("Account is already verified");
        }

        // Upload citizen ID card to Cloudinary
        String idCardUrl = cloudinaryService.uploadFile(idCardFile);
        log.info("Citizen ID card uploaded successfully: {}", idCardUrl);

        // Generate OTP
        String otpCode = generateOtp();
        LocalDateTime now = LocalDateTime.now();

        log.info("Generated OTP for user {}: {}", userId, otpCode);

        // Set verification data
        verification.setUser(user);
        verification.setCitizenIdCardUrl(idCardUrl);
        verification.setOtpCode(otpCode);
        verification.setOtpGeneratedAt(now);
        verification.setOtpExpiresAt(now.plusMinutes(10)); // OTP expires in 10 minutes
        verification.setStatus(VerificationStatus.PENDING);
        verification.setSubmittedAt(now);

        // Save verification
        verification = verificationRepository.save(verification);

        // Send OTP via email
        sendOtpEmail(user.getEmail(), otpCode, user.getFullName());

        log.info("OTP sent to user email: {}", user.getEmail());

        return mapToDTO(verification);
    }

    @Override
    @Transactional
    public AccountVerificationDTO verifyOtp(Long userId, String otpCode) {
        log.info("User {} verifying OTP", userId);

        AccountVerificationModel verification = verificationRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Verification request not found for user: " + userId));

        // Check if already verified
        if (verification.getStatus() == VerificationStatus.OTP_VERIFIED ||
            verification.getStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("OTP already verified");
        }

        // Check if OTP expired
        if (LocalDateTime.now().isAfter(verification.getOtpExpiresAt())) {
            throw new IllegalStateException("OTP has expired. Please request a new one.");
        }

        // Verify OTP
        if (!verification.getOtpCode().equals(otpCode)) {
            throw new IllegalArgumentException("Invalid OTP code");
        }

        // Update status
        verification.setStatus(VerificationStatus.OTP_VERIFIED);
        verification = verificationRepository.save(verification);

        log.info("OTP verified successfully for user: {}", userId);

        return mapToDTO(verification);
    }

    @Override
    @Transactional
    public void resendOtp(Long userId) {
        log.info("Resending OTP for user: {}", userId);

        AccountVerificationModel verification = verificationRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Verification request not found for user: " + userId));

        if (verification.getStatus() == VerificationStatus.APPROVED) {
            throw new IllegalStateException("Account is already verified");
        }

        // Generate new OTP
        String otpCode = generateOtp();
        LocalDateTime now = LocalDateTime.now();

        verification.setOtpCode(otpCode);
        verification.setOtpGeneratedAt(now);
        verification.setOtpExpiresAt(now.plusMinutes(10));

        verificationRepository.save(verification);

        // Send OTP via email
        UserModel user = verification.getUser();
        sendOtpEmail(user.getEmail(), otpCode, user.getFullName());

        log.info("New OTP sent to user email: {}", user.getEmail());
    }

    @Override
    public List<AccountVerificationDTO> getPendingVerifications() {
        log.info("Fetching all pending verifications");
        List<AccountVerificationModel> verifications =
                verificationRepository.findByStatusOrderBySubmittedAtDesc(VerificationStatus.OTP_VERIFIED);
        return verifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountVerificationDTO> getVerificationsByStatus(String status) {
        log.info("Fetching verifications with status: {}", status);
        VerificationStatus verificationStatus = VerificationStatus.valueOf(status.toUpperCase());
        List<AccountVerificationModel> verifications =
                verificationRepository.findByStatusOrderBySubmittedAtDesc(verificationStatus);
        return verifications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountVerificationDTO reviewVerification(Long verificationId, Long adminId, ReviewVerificationRequest request) {
        log.info("Admin {} reviewing verification {}", adminId, verificationId);

        AccountVerificationModel verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Verification not found with id: " + verificationId));

        AdminModel admin = adminsRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));

        if (verification.getStatus() != VerificationStatus.OTP_VERIFIED) {
            throw new IllegalStateException("Can only review verifications with OTP_VERIFIED status");
        }

        LocalDateTime now = LocalDateTime.now();
        verification.setReviewedByAdmin(admin);
        verification.setReviewedAt(now);

        if (request.getApproved()) {
            verification.setStatus(VerificationStatus.APPROVED);

            // Update user account status to ACTIVE if pending
            UserModel user = verification.getUser();
            if (user.getAccountStatus() == AccountStatus.PENDING) {
                user.setAccountStatus(AccountStatus.ACTIVE);
                userRepository.save(user);
            }

            // Send approval email
            sendApprovalEmail(user.getEmail(), user.getFullName());

            log.info("Verification approved for user: {}", user.getUserId());
        } else {
            verification.setStatus(VerificationStatus.REJECTED);
            verification.setRejectionReason(request.getRejectionReason());

            // Send rejection email
            sendRejectionEmail(verification.getUser().getEmail(),
                             verification.getUser().getFullName(),
                             request.getRejectionReason());

            log.info("Verification rejected for user: {}", verification.getUser().getUserId());
        }

        verification = verificationRepository.save(verification);

        return mapToDTO(verification);
    }

    @Override
    public AccountVerificationDTO getVerificationStatus(Long userId) {
        log.info("Fetching verification status for user: {}", userId);
        AccountVerificationModel verification = verificationRepository.findByUser_UserId(userId)
                .orElse(null);

        if (verification == null) {
            return null;
        }

        return mapToDTO(verification);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String email, String otpCode, String fullName) {
        String subject = "Verify Your Account - OTP Code";
        String htmlContent = buildOtpEmailContent(otpCode, fullName);

        try {
            log.info("Attempting to send OTP email to: {}", email);
            emailService.sendHtmlEmail(email, subject, htmlContent);
            log.info("OTP email sent successfully to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}: {} - Stack trace: ", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending OTP email to {}: {} - Stack trace: ", email, e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private void sendApprovalEmail(String email, String fullName) {
        String subject = "Account Verification Approved";
        String htmlContent = buildApprovalEmailContent(fullName);

        try {
            emailService.sendHtmlEmail(email, subject, htmlContent);
        } catch (MessagingException e) {
            log.error("Failed to send approval email to {}: {}", email, e.getMessage());
        }
    }

    private void sendRejectionEmail(String email, String fullName, String reason) {
        String subject = "Account Verification Rejected";
        String htmlContent = buildRejectionEmailContent(fullName, reason);

        try {
            emailService.sendHtmlEmail(email, subject, htmlContent);
        } catch (MessagingException e) {
            log.error("Failed to send rejection email to {}: {}", email, e.getMessage());
        }
    }

    private String buildOtpEmailContent(String otpCode, String fullName) {
        return String.format(
            "<html><body>" +
            "<h2>Account Verification</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Your OTP code for account verification is:</p>" +
            "<h1 style='color: #4CAF50; font-size: 32px;'>%s</h1>" +
            "<p>This code will expire in 10 minutes.</p>" +
            "<p>If you didn't request this verification, please ignore this email.</p>" +
            "<p>Best regards,<br>Freelancer Marketplace Team</p>" +
            "</body></html>",
            fullName, otpCode
        );
    }

    private String buildApprovalEmailContent(String fullName) {
        return String.format(
            "<html><body>" +
            "<h2>Account Verification Approved</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Congratulations! Your account verification has been approved by our admin team.</p>" +
            "<p>You can now access all features of the platform.</p>" +
            "<p>Best regards,<br>Freelancer Marketplace Team</p>" +
            "</body></html>",
            fullName
        );
    }

    private String buildRejectionEmailContent(String fullName, String reason) {
        return String.format(
            "<html><body>" +
            "<h2>Account Verification Rejected</h2>" +
            "<p>Dear %s,</p>" +
            "<p>Unfortunately, your account verification has been rejected.</p>" +
            "<p><strong>Reason:</strong> %s</p>" +
            "<p>Please contact our support team if you have any questions.</p>" +
            "<p>Best regards,<br>Freelancer Marketplace Team</p>" +
            "</body></html>",
            fullName, reason != null ? reason : "Not provided"
        );
    }

    private AccountVerificationDTO mapToDTO(AccountVerificationModel verification) {
        AccountVerificationDTO dto = new AccountVerificationDTO();
        dto.setVerificationId(verification.getVerificationId());
        dto.setUserId(verification.getUser().getUserId());
        dto.setUsername(verification.getUser().getUsername());
        dto.setFullName(verification.getUser().getFullName());
        dto.setEmail(verification.getUser().getEmail());
        dto.setCitizenIdCardUrl(verification.getCitizenIdCardUrl());
        dto.setStatus(verification.getStatus());
        dto.setRejectionReason(verification.getRejectionReason());
        dto.setSubmittedAt(verification.getSubmittedAt());
        dto.setReviewedAt(verification.getReviewedAt());

        if (verification.getReviewedByAdmin() != null) {
            dto.setReviewedByAdminName(verification.getReviewedByAdmin().getFirstName() +
                                       " " + verification.getReviewedByAdmin().getLastName());
        }

        return dto;
    }
}

