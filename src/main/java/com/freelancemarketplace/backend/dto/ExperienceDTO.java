package com.freelancemarketplace.backend.dto;

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
