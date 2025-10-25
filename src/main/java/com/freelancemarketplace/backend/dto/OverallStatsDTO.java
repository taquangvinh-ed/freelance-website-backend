package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OverallStatsDTO {
    double totalEarnings;
    int activeProjects;
    int pendingReview;
    double hourlyRate;
}
