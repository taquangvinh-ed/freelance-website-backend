package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import com.freelancemarketplace.backend.project.application.service.ProjectCrudService;
import com.freelancemarketplace.backend.project.application.service.ProjectSearchService;
import com.freelancemarketplace.backend.project.application.service.ProjectService;
import com.freelancemarketplace.backend.project.application.service.ProjectStatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProjectServiceFacadeImp implements ProjectService {

    private final ProjectCrudService projectCrudService;
    private final ProjectSearchService projectSearchService;
    private final ProjectStatisticsService projectStatisticsService;

    public ProjectServiceFacadeImp(ProjectCrudService projectCrudService,
                                 ProjectSearchService projectSearchService,
                                 ProjectStatisticsService projectStatisticsService) {
        this.projectCrudService = projectCrudService;
        this.projectSearchService = projectSearchService;
        this.projectStatisticsService = projectStatisticsService;
    }

    // ==================== CRUD Operations (delegates to ProjectCrudService) ====================

    @Override
    public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
        return projectCrudService.createProject(clientId, request);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        return projectCrudService.updateProject(projectId, projectDTO);
    }

    @Override
    public void deleteProject(Long projectId) {
        projectCrudService.deleteProject(projectId);
    }

    @Override
    public List<ProjectDTO> getAllProject() {
        return projectCrudService.getAllProjects();
    }

    @Override
    public ProjectDTO findProjectById(Long projectId) {
        return projectCrudService.findProjectById(projectId);
    }

    @Override
    public List<ProjectDTO> getProjectBySkill(Long skillId) {
        return projectCrudService.getProjectsBySkill(skillId);
    }

    @Override
    public void assignSkillToProject(Long projectId, Long skillId) {
        projectCrudService.assignSkillToProject(projectId, skillId);
    }

    @Override
    public void removeSkillFromProject(Long projectId, Long skillId) {
        projectCrudService.removeSkillFromProject(projectId, skillId);
    }

    // ==================== Search Operations (delegates to ProjectSearchService) ====================

    @Override
    public Page<ProjectDTO> filter(String keyword, List<String> skillNames,
                                   BigDecimal minRate, BigDecimal maxRate, Boolean isHourly, String duration,
                                   String level,
                                   String workload, Pageable pageable) {
        return projectSearchService.searchProjects(keyword, skillNames, minRate, maxRate, isHourly, duration, level, workload, pageable);
    }

    @Override
    public Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable) {
        return projectSearchService.autocompleteSearch(keyword, limit, pageable);
    }

    // ==================== Statistics Operations (delegates to ProjectStatisticsService) ====================

    @Override
    public long countAllProjects() {
        return projectStatisticsService.countAllProjects();
    }

    @Override
    public long getNewProjectCountToday() {
        return projectStatisticsService.getNewProjectCountToday();
    }

    @Override
    public long getNewProjectCountWeekly() {
        return projectStatisticsService.getNewProjectCountWeekly();
    }

    @Override
    public long getActiveProjectCount() {
        return projectStatisticsService.getActiveProjectCount();
    }

    @Override
    public long getCompletedProjectCount() {
        return projectStatisticsService.getCompletedProjectCount();
    }
}
