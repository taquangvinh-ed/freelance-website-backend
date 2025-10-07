package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);

    void deleteProject(Long projectId);

    List<ProjectDTO> getAllProject();

    ProjectDTO findProjectById(Long projectId);

    List<ProjectDTO> getProjectBySkill(Long skillId);

    void assignSkillToProject (Long projectId, Long skillId);


    @Transactional
    void removeSkillFromProject(Long projectId, Long skillId);

    Page<ProjectDTO> advancedSearchProjects(String keyword, Long categoryId, List<String> skillNames,
                                            Double minRate, Double maxRate, Boolean isHourly, String status, Pageable pageable);
}
