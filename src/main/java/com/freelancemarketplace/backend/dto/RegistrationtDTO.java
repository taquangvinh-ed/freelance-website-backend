package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationtDTO {

    private Long userId;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String role; // e.g., "FREELANCER", "CLIENT", "ADMIN"

    private String password;

    private String token;

}
