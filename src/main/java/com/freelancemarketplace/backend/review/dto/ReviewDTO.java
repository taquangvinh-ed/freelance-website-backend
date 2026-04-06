package com.freelancemarketplace.backend.review.dto;

import com.freelancemarketplace.backend.review.domain.enums.ReviewTypes;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {

    private Long testimonialId;

    private Integer ratingScore;

    private String comment;

    private Timestamp datePosted;

    private Boolean isResponded;

    private String reviewerRole;

    private String type;

    private Long freelancerId;

    private Long contractId;

    private Long clientId;

    private Long teamId;

}
