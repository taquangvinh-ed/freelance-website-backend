package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProjectTrackingDTO {
    private Long contractId;
    private String projectTitle;
    private String freelancerName;
    private String freelancerProfileUrl; // Đường dẫn đến trang Freelancer
    private double progressPercentage;
    private Timestamp deadline;
    private String status;
}
