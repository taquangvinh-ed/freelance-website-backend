package com.freelancemarketplace.backend.project.application.service;

import com.freelancemarketplace.backend.project.domain.model.ProjectModel;

/**
 * Application Service: Project Embedding & Vector Operations (SRP)
 * This service handles vector/embedding generation for projects for search & recommendations
 */
public interface ProjectEmbeddingService {
    
    /**
     * Generate title embedding
     * @param projectId Project ID
     * @param title Project title
     */
    void generateTitleEmbedding(Long projectId, String title);
    
    /**
     * Generate description embedding
     * @param projectId Project ID
     * @param description Project description
     */
    void generateDescriptionEmbedding(Long projectId, String description);
    
    /**
     * Generate skill vector (composite vector from skills)
     * @param project Project entity
     * @return Skill vector representation
     */
    byte[] generateSkillVector(ProjectModel project);
    
    /**
     * Update all embeddings for a project
     * @param project Project entity to update embeddings
     */
    void updateProjectEmbeddings(ProjectModel project);
}

