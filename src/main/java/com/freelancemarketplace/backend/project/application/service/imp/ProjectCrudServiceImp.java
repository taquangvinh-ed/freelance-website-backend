package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.category.infrastructure.repository.CategoriesRepository;
import com.freelancemarketplace.backend.client.exception.ClientNotFoundException;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.exceptionHandling.ApiException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.project.exception.ProjectNotFoundException;
import com.freelancemarketplace.backend.skill.exception.SkillNotFoundException;
import com.freelancemarketplace.backend.project.infrastructure.mapper.ProjectMapper;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.project.infrastructure.repository.BudgetsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.recommendation.application.service.EmbeddingService;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import com.freelancemarketplace.backend.project.application.service.ProjectCrudService;
import com.freelancemarketplace.backend.skill.infrastructure.repository.SkillsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation: Project CRUD Service (SRP)
 * 
 * Demonstrates:
 * - SRP: Only handles CRUD operations
 * - Uses constructor injection for dependencies
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectCrudServiceImp implements ProjectCrudService {

    private final ProjectsRepository projectsRepository;
    private final ProjectMapper projectMapper;
    private final SkillsRepository skillsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BudgetsRepository budgetsRepository;
    private final EmbeddingService embeddingService;
    private final ClientsRepository clientsRepository;

    @Override
    @Transactional
    public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                () -> new ClientNotFoundException("Client with id: " + clientId + " not found")
        );

        if (request.getBudget() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, "Budget is required for the project.");
        }
        if (request.getCategory() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, "Category is required for the project.");
        }
        if (request.getSkills() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, "Skills is required for the project");
        }

        ProjectModel newProject = projectMapper.toEntity(request);
        BudgetModel budgetInsideNewProject = newProject.getBudget();
        budgetInsideNewProject.setProject(newProject);
        newProject.setStatus(ProjectStatus.OPEN);

        if (request.getTitle() != null) {
            newProject.setTitleEmbedding(embeddingService.generateEmbedding(request.getTitle()));
        }
        if (request.getDescription() != null) {
            newProject.setDescriptionEmbedding(embeddingService.generateEmbedding(request.getDescription()));
        }

        newProject.setClient(client);
        newProject.setSkillVector(embeddingService.generateProjectSkillVector(newProject));

        ProjectModel savedProject = projectsRepository.save(newProject);
        log.info("Created project with ID: {}", savedProject.getProjectId());
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectModel updatedProject = projectMapper.partialUpdate(projectDTO, project);
        ProjectModel savedProject = projectsRepository.save(updatedProject);
        log.info("Updated project with ID: {}", projectId);
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        if (!projectsRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project with id: " + projectId + " not found.");
        }
        projectsRepository.deleteById(projectId);
        log.info("Deleted project with ID: {}", projectId);
    }

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<ProjectModel> projectList = projectsRepository.findAll();
        return projectMapper.toDTOs(projectList);
    }

    @Override
    public ProjectDTO findProjectById(Long projectId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found."));
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public void assignSkillToProject(Long projectId, Long skillId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found."));
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new SkillNotFoundException("Skill with id: " + skillId + " not found.")
        );

        project.getSkills().add(skill);
        project.setSkillVector(embeddingService.generateProjectSkillVector(project));
        projectsRepository.save(project);
        log.info("Assigned skill {} to project {}", skillId, projectId);
    }

    @Override
    @Transactional
    public void removeSkillFromProject(Long projectId, Long skillId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + projectId + " not found."));
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new SkillNotFoundException("Skill with id: " + skillId + " not found.")
        );

        project.getSkills().remove(skill);
        project.setSkillVector(embeddingService.generateProjectSkillVector(project));
        projectsRepository.save(project);
        log.info("Removed skill {} from project {}", skillId, projectId);
    }

    @Override
    public List<ProjectDTO> getProjectsBySkill(Long skillId) {
        List<ProjectModel> projects = projectsRepository.findBySkills_SkillId(skillId);
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }
}
