package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendFreelancerDTO {

    private Long freelancerId;
    private String avatar;
    private String title;
    private String firstName;
    private String lastName;
    private double rating;
    private String description;
    private double hourlyRate;

}
