package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.UserTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    private String username;

    private String email;

    private String role; // e.g., "FREELANCER", "CLIENT", "ADMIN", "COMPANY"

    private String password;
    private UserTypes userType;
    private Boolean isVerified;
    private Boolean isBlocked;
}
