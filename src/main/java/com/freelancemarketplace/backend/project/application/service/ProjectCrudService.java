package com.freelancemarketplace.backend.project.application.service;

import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Application Service: Project CRUD Operations (SRP - Single Responsibility Principle)
 * This service handles ONLY create, read, update, delete operations
 */
public interface ProjectCrudService {
    
    /**
     * Create a new project
     * @param clientId Client ID
     * @param request Project creation request
     * @return Created project DTO
     */
    @Transactional
    ProjectDTO createProject(Long clientId, CreateProjectRequest request);
    
    /**
     * Update an existing project
     * @param projectId Project ID
     * @param projectDTO Project data to update
     * @return Updated project DTO
     */
    @Transactional
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    
    /**
     * Delete a project
     * @param projectId Project ID
     */
    @Transactional
    void deleteProject(Long projectId);
    
    /**
     * Get all projects
     * @return List of all project DTOs
     */
    List<ProjectDTO> getAllProjects();
    
    /**
     * Get project by ID
     * @param projectId Project ID
     * @return Project DTO
     */
    ProjectDTO findProjectById(Long projectId);
    
    /**
     * Assign skill to project
     * @param projectId Project ID
     * @param skillId Skill ID
     */
    @Transactional
    void assignSkillToProject(Long projectId, Long skillId);
    
    /**
     * Remove skill from project
     * @param projectId Project ID
     * @param skillId Skill ID
     */
    @Transactional
    void removeSkillFromProject(Long projectId, Long skillId);
    
    /**
     * Get projects by skill
     * @param skillId Skill ID
     * @return List of project DTOs
     */
    List<ProjectDTO> getProjectsBySkill(Long skillId);
}
