package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FreelancerInfoDTO {
    private Long freelancerId;
    private String avatar;
    private String title;
    private String firstName;
    private String lastName;
    private double rating;
    private String description;
    private double hourlyRate;
    private Set<ExperienceDTO> experiences;
    private Set<CertificateDTO> certificates;
    private Set<TestimonialDTO> testimonials;
    private int reviews;
    private double averageScore;
}
