package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class TestimonialDTO {
    private Long testimonialId;

    private String rating;

    private String comment;

    // The date that the testimonial was given
    private Timestamp date;


    private Boolean testiminialBack;

    private Long freelancerId;

    private Long projectId;


    private Long clientId;


    private Long companyId;

    private Long teamId;

}
