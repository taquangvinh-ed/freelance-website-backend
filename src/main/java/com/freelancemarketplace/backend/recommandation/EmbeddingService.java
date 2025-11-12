package com.freelancemarketplace.backend.recommandation;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;

public interface EmbeddingService {

    public byte[] generateEmbedding(String text);

    byte[] generateSkillVector(FreelancerModel freelancer);

    String generateSummary(FreelancerModel freelancer);

    public void generateProjectSkillVector(ProjectModel project);

}
