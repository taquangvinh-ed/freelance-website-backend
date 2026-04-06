package com.freelancemarketplace.backend.service.impl;

import com.freelancemarketplace.backend.application.port.ProjectCrudPort;
import com.freelancemarketplace.backend.application.service.ProjectCRUDService;
import com.freelancemarketplace.backend.application.service.ProjectEmbeddingService;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.domain.exception.BaseApplicationException;
import com.freelancemarketplace.backend.domain.exception.ErrorCode;
import com.freelancemarketplace.backend.infrastructure.mapper.ProjectMapper;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation: Project CRUD Operations (SRP - Single Responsibility Principle)
 * 
 * This service is responsible ONLY for Create, Read, Update, Delete operations
 * Demonstrates:
 * - DIP: Depends on ProjectCrudPort (abstraction), not concrete repository
 * - SRP: Only handles CRUD, not search/stats/skills
 * - OCP: Can be extended without modification
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectCRUDServiceImpl implements ProjectCRUDService {
    
    private final ProjectCrudPort projectCrudPort;
    private final ClientsRepository clientsRepository;
    private final ProjectMapper projectMapper;
    private final ProjectEmbeddingService projectEmbeddingService;
    
    @Override
    @Transactional
    public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
        log.info("Creating new project for client: {}", clientId);
        
        // Validate input
        if (request == null) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Project request cannot be null"
            );
        }
        
        if (request.getBudget() == null) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Budget is required for the project"
            );
        }
        
        if (request.getCategory() == null) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Category is required for the project"
            );
        }
        
        if (request.getSkills() == null || request.getSkills().isEmpty()) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "At least one skill is required for the project"
            );
        }
        
        // Find client
        ClientModel client = clientsRepository.findById(clientId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Client not found with id: " + clientId
                ));
        
        // Map DTO to entity
        ProjectModel newProject = projectMapper.toEntity(request);
        
        // Set client
        newProject.setClient(client);
        
        // Initialize budget
        BudgetModel budgetInsideNewProject = newProject.getBudget();
        if (budgetInsideNewProject != null) {
            budgetInsideNewProject.setProject(newProject);
        }
        
        // Set default status
        newProject.setStatus(ProjectStatus.OPEN);
        
        // Generate embeddings
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            projectEmbeddingService.generateTitleEmbedding(0L, request.getTitle());
        }
        
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            projectEmbeddingService.generateDescriptionEmbedding(0L, request.getDescription());
        }
        
        // Generate skill vector
        projectEmbeddingService.generateSkillVector(newProject);
        
        // Save project
        ProjectModel savedProject = projectCrudPort.save(newProject);
        
        log.info("Project created successfully with id: {}", savedProject.getProjectId());
        
        return projectMapper.toDto(savedProject);
    }
    
    @Override
    public ProjectDTO getProjectById(Long projectId) {
        log.info("Getting project: {}", projectId);
        
        ProjectModel project = projectCrudPort.findById(projectId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.PROJECT_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Project not found with id: " + projectId
                ));
        
        return projectMapper.toDto(project);
    }
    
    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        log.info("Updating project: {}", projectId);
        
        ProjectModel project = projectCrudPort.findById(projectId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.PROJECT_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Project not found with id: " + projectId
                ));
        
        // Partial update using mapper
        ProjectModel updatedProject = projectMapper.partialUpdate(projectDTO, project);
        
        // Update embeddings if content changed
        if (projectDTO.getTitle() != null && !projectDTO.getTitle().isEmpty()) {
            projectEmbeddingService.generateTitleEmbedding(projectId, projectDTO.getTitle());
        }
        
        if (projectDTO.getDescription() != null && !projectDTO.getDescription().isEmpty()) {
            projectEmbeddingService.generateDescriptionEmbedding(projectId, projectDTO.getDescription());
        }
        
        // Save updated project
        ProjectModel savedProject = projectCrudPort.save(updatedProject);
        
        log.info("Project updated successfully");
        
        return projectMapper.toDto(savedProject);
    }
    
    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        log.info("Deleting project: {}", projectId);
        
        if (!projectCrudPort.existsById(projectId)) {
            throw new BaseApplicationException(
                    ErrorCode.PROJECT_NOT_FOUND,
                    HttpStatus.NOT_FOUND,
                    "Project not found with id: " + projectId
            );
        }
        
        projectCrudPort.deleteById(projectId);
        
        log.info("Project deleted successfully");
    }
}

