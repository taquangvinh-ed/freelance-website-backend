package com.freelancemarketplace.backend.service.impl;

import com.freelancemarketplace.backend.application.service.ProjectEmbeddingService;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.recommendation.application.service.EmbeddingService;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation: Project Embedding & Vector Operations (SRP)
 * 
 * Demonstrates:
 * - SRP: Only handles embedding/vector generation
 * - Responsibility delegation to EmbeddingService for actual embedding logic
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectEmbeddingServiceImpl implements ProjectEmbeddingService {
    
    private final ProjectsRepository projectsRepository;
    private final EmbeddingService embeddingService;
    
    @Override
    @Transactional
    public void generateTitleEmbedding(Long projectId, String title) {
        log.info("Generating title embedding for project: {}", projectId);

        if (title == null || title.isEmpty()) {
            log.warn("Title is empty, skipping embedding generation");
            return;
        }

        try {
            byte[] titleEmbedding = embeddingService.generateEmbedding(title);

            // If project exists, update it
            if (projectId != null && projectId > 0) {
                projectsRepository.findById(projectId).ifPresent(project -> {
                    project.setTitleEmbedding(titleEmbedding);
                    projectsRepository.save(project);
                    log.info("Title embedding generated and saved");
                });
            }
        } catch (Exception e) {
            log.error("Error generating title embedding", e);
            // Continue without embedding - not critical
        }
    }
    
    @Override
    @Transactional
    public void generateDescriptionEmbedding(Long projectId, String description) {
        log.info("Generating description embedding for project: {}", projectId);

        if (description == null || description.isEmpty()) {
            log.warn("Description is empty, skipping embedding generation");
            return;
        }

        try {
            byte[] descriptionEmbedding = embeddingService.generateEmbedding(description);

            // If project exists, update it
            if (projectId != null && projectId > 0) {
                projectsRepository.findById(projectId).ifPresent(project -> {
                    project.setDescriptionEmbedding(descriptionEmbedding);
                    projectsRepository.save(project);
                    log.info("Description embedding generated and saved");
                });
            }
        } catch (Exception e) {
            log.error("Error generating description embedding", e);
            // Continue without embedding - not critical
        }
    }
    
    @Override
    public byte[] generateSkillVector(ProjectModel project) {
        log.info("Generating skill vector for project: {}", project != null ? project.getProjectId() : null);

        if (project == null || project.getSkills() == null || project.getSkills().isEmpty()) {
            log.warn("Project has no skills, returning empty vector");
            return new byte[0];
        }

        try {
            String skillsText = String.join(", ",
                    project.getSkills().stream()
                            .map(skill -> skill.getName())
                            .toArray(String[]::new)
            );

            byte[] skillVector = embeddingService.generateEmbedding(skillsText);
            log.info("Skill vector generated successfully");
            return skillVector;
        } catch (Exception e) {
            log.error("Error generating skill vector", e);
            return new byte[0];
        }
    }
    
    @Override
    @Transactional
    public void updateProjectEmbeddings(ProjectModel project) {
        log.info("Updating all embeddings for project: {}", project != null ? project.getProjectId() : null);

        if (project == null) {
            log.warn("Project is null, cannot update embeddings");
            return;
        }

        if (project.getTitle() != null && !project.getTitle().isEmpty()) {
            generateTitleEmbedding(project.getProjectId(), project.getTitle());
        }

        if (project.getDescription() != null && !project.getDescription().isEmpty()) {
            generateDescriptionEmbedding(project.getProjectId(), project.getDescription());
        }

        byte[] skillVector = generateSkillVector(project);
        project.setSkillVector(skillVector);
        projectsRepository.save(project);

        log.info("All embeddings updated successfully");
    }
}
