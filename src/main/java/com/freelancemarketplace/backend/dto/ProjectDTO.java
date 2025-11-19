package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;

    private String title;
    private String description;

    private ProjectStatus status;

    private Timestamp startDate;
    private Timestamp endDate;

    // If the project is an internship
    private Boolean isInternship;

    private Set<SkillDTO> skills = new HashSet<>();

    private BudgetDTO budget;

    private ProjectScopeDTO scope;

    private CategoryDTO category;

    private Long clientId;

    private AdvancedPreferencesDTO advancedPreferences;

    private Timestamp createdAt;
}
