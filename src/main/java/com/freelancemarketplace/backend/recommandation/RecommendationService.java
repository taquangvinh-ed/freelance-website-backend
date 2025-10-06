package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.dto.ProjectDTO;

import java.util.List;

public interface RecommendationService {

    List<ProjectDTO> recommendProjects(Long freelancerId);
}
