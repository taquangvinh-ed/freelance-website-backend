package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.domain.model.ProjectModel;
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
