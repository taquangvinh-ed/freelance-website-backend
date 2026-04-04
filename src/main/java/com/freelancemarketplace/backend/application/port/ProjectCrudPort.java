package com.freelancemarketplace.backend.application.port;

import com.freelancemarketplace.backend.domain.model.ProjectModel;

import java.util.List;

/**
 * Port: Project Repository Interface (ISP - Interface Segregation Principle)
 * Allows business logic to depend on abstractions, not concrete repositories
 */
public interface ProjectCrudPort extends BaseCrudPort<ProjectModel, Long> {
    
    /**
     * Find projects by client ID
     * @param clientId Client ID
     * @return List of projects
     */
    List<ProjectModel> findByClientId(Long clientId);
    
    /**
     * Find active projects
     * @return List of active projects
     */
    List<ProjectModel> findActiveProjects();
    
    /**
     * Count active projects
     * @return Count of active projects
     */
    long countActiveProjects();
}

