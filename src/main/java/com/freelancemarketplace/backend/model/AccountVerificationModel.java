package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "account_verifications")
public class AccountVerificationModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    private UserModel user;

    @Column(nullable = false)
    private String citizenIdCardUrl;

    @Column(nullable = false, length = 6)
    private String otpCode;

    @Column(nullable = false)
    private LocalDateTime otpGeneratedAt;

    @Column(nullable = false)
    private LocalDateTime otpExpiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus status = VerificationStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "reviewedByAdminId")
    private AdminModel reviewedByAdmin;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private LocalDateTime reviewedAt;

    private LocalDateTime submittedAt;
}

