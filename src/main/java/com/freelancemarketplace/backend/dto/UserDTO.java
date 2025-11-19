package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.enums.UserRoles;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    private String username;

    private String email;

    private String role; // e.g., "FREELANCER", "CLIENT", "ADMIN"

    private String password;
    private Boolean isVerified;
    private Boolean isBlocked;
    private String accountStatus;
    private String disableReason;
    private Timestamp disableAt;
}
