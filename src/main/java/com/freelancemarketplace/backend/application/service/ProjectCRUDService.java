package com.freelancemarketplace.backend.application.service;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.api.request.CreateProjectRequest;

/**
 * Application Service: Project CRUD Operations (SRP - Single Responsibility Principle)
 * This service is responsible ONLY for Create, Read, Update, Delete operations
 * Other concerns like search, statistics, etc. are handled by separate services
 */
public interface ProjectCRUDService {
    
    /**
     * Create a new project
     * @param clientId ID of client creating the project
     * @param request Project creation request
     * @return Created project DTO
     */
    ProjectDTO createProject(Long clientId, CreateProjectRequest request);
    
    /**
     * Get project by ID
     * @param projectId Project ID
     * @return Project DTO
     */
    ProjectDTO getProjectById(Long projectId);
    
    /**
     * Update existing project
     * @param projectId Project ID
     * @param projectDTO Updated project data
     * @return Updated project DTO
     */
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    
    /**
     * Delete project
     * @param projectId Project ID
     */
    void deleteProject(Long projectId);
}

