package com.freelancemarketplace.backend.recommendation.application.service;

import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScoredProject {
    private ProjectModel project;
    private Double score;
}
