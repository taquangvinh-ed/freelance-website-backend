package com.freelancemarketplace.backend.dto;
import com.freelancemarketplace.backend.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountVerificationDTO {
    private Long verificationId;
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String citizenIdCardUrl;
    private VerificationStatus status;
    private String rejectionReason;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String reviewedByAdminName;
}
