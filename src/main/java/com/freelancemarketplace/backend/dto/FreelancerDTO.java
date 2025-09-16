package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FreelancerDTO {
    private Long freelancerId;
    private String firstName;
    private String lastName;
    private String title;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private Boolean isVerified;
    private Double hourlyRate;
}
