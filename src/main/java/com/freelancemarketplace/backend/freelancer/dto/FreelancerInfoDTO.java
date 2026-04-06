package com.freelancemarketplace.backend.freelancer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import com.freelancemarketplace.backend.certification.dto.CertificateDTO;
import com.freelancemarketplace.backend.review.dto.TestimonialDTO;

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
