package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.RecommendFreelancerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecommendationService {

    Page<ProjectDTO> recommendProjects(Long freelancerId, Pageable pageable);


    Page<RecommendFreelancerDTO> recommendFreelancers(Long projectId, Pageable pageable);
}
