package com.freelancemarketplace.backend.freelancer.dto;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {

    private Long experienceId;
    private String title;
    private String organization;
    private String startDate;
    private String endDate;
    private String description;

    private Long locationId;

    private Long freelancerId;

}
