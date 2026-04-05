package com.freelancemarketplace.backend.infrastructure.persistence;

import com.freelancemarketplace.backend.application.port.ProjectCrudPort;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository Adapter: Project CRUD Repository (DIP - Dependency Inversion Principle)
 * Implements ProjectCrudPort for database operations
 * Abstracts away JPA repository details from business logic
 */
@Repository
@AllArgsConstructor
@Slf4j
public class ProjectRepositoryAdapter implements ProjectCrudPort {
    
    private final ProjectsRepository projectsRepository;
    
    @Override
    public ProjectModel save(ProjectModel entity) {
        log.debug("Saving project: {}", entity.getProjectId());
        return projectsRepository.save(entity);
    }
    
    @Override
    public Optional<ProjectModel> findById(Long id) {
        log.debug("Finding project by ID: {}", id);
        return projectsRepository.findById(id);
    }
    
    @Override
    public List<ProjectModel> findAll() {
        log.debug("Finding all projects");
        return projectsRepository.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        log.debug("Deleting project by ID: {}", id);
        projectsRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        log.debug("Checking if project exists: {}", id);
        return projectsRepository.existsById(id);
    }
    
    @Override
    public long count() {
        log.debug("Counting all projects");
        return projectsRepository.count();
    }
    
    @Override
    public ProjectModel update(ProjectModel entity) {
        log.debug("Updating project: {}", entity.getProjectId());
        return projectsRepository.save(entity);
    }
    
    @Override
    public List<ProjectModel> findByClientId(Long clientId) {
        log.debug("Finding projects for client: {}", clientId);
        return projectsRepository.findByClient_ClientId(clientId);
    }
    
    @Override
    public List<ProjectModel> findActiveProjects() {
        log.debug("Finding active projects");
        return projectsRepository.findByStatus(ProjectStatus.OPEN);
    }
    
    @Override
    public long countActiveProjects() {
        log.debug("Counting active projects");
        List<ProjectModel> activeProjects = findActiveProjects();
        return activeProjects.size();
    }
}

