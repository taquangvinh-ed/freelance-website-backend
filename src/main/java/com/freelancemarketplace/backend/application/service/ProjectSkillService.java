package com.freelancemarketplace.backend.application.service;

/**
 * Application Service: Project Skill Management (SRP - Single Responsibility Principle)
 * This service handles skill assignment and removal from projects
 */
public interface ProjectSkillService {
    
    /**
     * Assign a skill to a project
     * @param projectId Project ID
     * @param skillId Skill ID
     */
    void assignSkillToProject(Long projectId, Long skillId);
    
    /**
     * Remove a skill from a project
     * @param projectId Project ID
     * @param skillId Skill ID
     */
    void removeSkillFromProject(Long projectId, Long skillId);
}

