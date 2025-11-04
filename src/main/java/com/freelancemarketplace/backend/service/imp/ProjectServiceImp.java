package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.enums.ProjectStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectMapper;
import com.freelancemarketplace.backend.model.BudgetModel;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import com.freelancemarketplace.backend.repository.*;
import com.freelancemarketplace.backend.service.ProjectService;
import com.freelancemarketplace.backend.specification.ProjectSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectServiceImp implements ProjectService {

    private final ProjectsRepository projectsRepository;
    private final ProjectMapper projectMapper;
    private final SkillsRepository skillsRepository;
    private final CategoriesRepository categoriesRepository;
    private final BudgetsRepository budgetsRepository;
    private final EmbeddingService embeddingService;
    private final ClientsRepository clientsRepository;


    @Override
    @Transactional
    public ProjectDTO createProject(Long clientId, ProjectDTO projectDTO) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                ()->new ResourceNotFoundException("Client with id: " + clientId + " not found")
        );


        if (projectDTO.getBudget() == null) {
            throw new IllegalArgumentException("Budget is required for the project.");
        }

        if (projectDTO.getCategory() == null)
            throw new IllegalArgumentException("Category is required for the project.");

        if (projectDTO.getSkills() == null)
            throw new IllegalArgumentException("Skills is required for the project");

        ProjectModel newProject = projectMapper.toEntity(projectDTO);
        BudgetModel budgetInsideNewProject = newProject.getBudget();
        budgetInsideNewProject.setProject(newProject);
        newProject.setStatus(ProjectStatus.IN_PROGRESS);

        if (projectDTO.getTitle() != null)
            newProject.setTitleEmbedding(embeddingService.generateEmbedding(projectDTO.getTitle()));

        if (projectDTO.getDescription() != null)
            newProject.setDescriptionEmbedding(embeddingService.generateEmbedding(projectDTO.getDescription()));

        newProject.setClient(client);

        ProjectModel savedProject = projectsRepository.save(newProject);
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectModel updatedProject = projectMapper.partialUpdate(projectDTO, project);

        ProjectModel savedProject = projectsRepository.save(updatedProject);

        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        Boolean isExisted = projectsRepository.existsById(projectId);
        if (!isExisted) {
            throw new ResourceNotFoundException("Project with id: " + projectId + " not found.");
        }
        projectsRepository.deleteById(projectId);
    }

    @Override
    public List<ProjectDTO> getAllProject() {
        List<ProjectModel> projectList = projectsRepository.findAll();
        return projectMapper.toDTOs(projectList);
    }

    @Override
    public ProjectDTO findProjectById(Long projectId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found."));
        return projectMapper.toDto(project);
    }

    @Override
    public List<ProjectDTO> getProjectBySkill(Long skillId) {

        return List.of();
    }

    @Override
    @Transactional
    public void assignSkillToProject(Long projectId, Long skillId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found."));
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found.")
        );

        project.getSkills().add(skill);
        projectsRepository.save(project);
    }

    @Transactional
    @Override
    public void removeSkillFromProject(Long projectId, Long skillId) {
        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project with id: " + projectId + " not found."));
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found.")
        );

        project.getSkills().remove(skill);
        projectsRepository.save(project);
    }

    @Override
    public Page<ProjectDTO> filter(List<String> skillNames,
                                   BigDecimal minRate, BigDecimal maxRate, Boolean isHourly, Pageable pageable) {
        try {
            // Tạo Specification cho truy vấn
            Specification<ProjectModel> spec = ProjectSpecification.filter(
                     skillNames, minRate, maxRate, isHourly);

            // Thực thi truy vấn với Specification và phân trang
            Page<ProjectModel> projectModels = projectsRepository.findAll(spec, pageable);

            // Chuyển đổi sang DTO
            List<ProjectDTO> dtoList = projectModels.getContent().stream()
                    .map(projectMapper::toDto)
                    .collect(Collectors.toList());

            // Tạo đối tượng Page
            return new PageImpl<>(dtoList, pageable, projectModels.getTotalElements());
        } catch (Exception e) {
            log.error("Error executing JPA Specification search: {}", e.getMessage(), e);
            throw new RuntimeException("Error executing JPA Specification search", e);
        }
    }

    @Override
    public Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable) {
        try {
            // Tạo Specification cho tìm kiếm autocomplete
            Specification<ProjectModel> spec = ProjectSpecification.autocompleteSearch(keyword, limit);

            // Thực thi truy vấn với giới hạn số lượng kết quả
            Page<ProjectModel> projectPage = projectsRepository.findAll(spec, pageable);

            // Chuyển đổi sang DTO
            List<ProjectDTO> projects = projectPage.getContent().stream()
                    .map(projectMapper::toDto)
                    .collect(Collectors.toList());

            return new PageImpl<>(projects, pageable, projectPage.getTotalElements());
        } catch (Exception e) {
            log.error("Error executing autocomplete search: {}", e.getMessage(), e);
            throw new RuntimeException("Error executing autocomplete search", e);
        }
    }


}
