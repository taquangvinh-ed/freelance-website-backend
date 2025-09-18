package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;

    private String title;
    private String description;

    private ProjectStatus status;

    private Double budgetAmount;
    private Integer durationDays;
    private Integer connections;
    private Timestamp startDate;
    private Timestamp endDate;

    // If the project is an internship
    private Boolean isInternship;
}
