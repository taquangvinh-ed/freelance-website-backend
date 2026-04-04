package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;

public interface EmbeddingService {

    public byte[] generateEmbedding(String text);

    byte[] generateSkillVector(FreelancerModel freelancer);

    String generateSummary(FreelancerModel freelancer);

    public byte[] generateProjectSkillVector(ProjectModel project);

}
