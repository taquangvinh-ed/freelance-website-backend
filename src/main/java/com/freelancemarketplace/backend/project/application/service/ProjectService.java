package com.freelancemarketplace.backend.project.application.service;

import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Project Service Facade
 * 
 * This interface provides a unified API for project operations.
 * It delegates to focused services following the Single Responsibility Principle:
 * - ProjectCrudService: CRUD operations
 * - ProjectSearchService: Search and filtering
 * - ProjectStatisticsService: Statistics and counting
 */
public interface ProjectService {

    // ==================== CRUD Operations (delegates to ProjectCrudService) ====================

    ProjectDTO createProject(Long clientId, CreateProjectRequest request);

    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);

    void deleteProject(Long projectId);

    List<ProjectDTO> getAllProject();

    ProjectDTO findProjectById(Long projectId);

    List<ProjectDTO> getProjectBySkill(Long skillId);

    void assignSkillToProject(Long projectId, Long skillId);

    void removeSkillFromProject(Long projectId, Long skillId);

    // ==================== Search Operations (delegates to ProjectSearchService) ====================

    Page<ProjectDTO> filter(String keyword, List<String> skillNames,
                            BigDecimal minRate, BigDecimal maxRate, Boolean isHourly, String duration,
                            String level,
                            String workload, Pageable pageable);

    Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable);

    // ==================== Statistics Operations (delegates to ProjectStatisticsService) ====================

    long countAllProjects();

    long getNewProjectCountToday();

    long getNewProjectCountWeekly();

    long getActiveProjectCount();

    long getCompletedProjectCount();
}
