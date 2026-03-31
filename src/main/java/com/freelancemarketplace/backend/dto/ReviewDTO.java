package com.freelancemarketplace.backend.dto;

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
