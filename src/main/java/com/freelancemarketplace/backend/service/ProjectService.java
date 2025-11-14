package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.request.CreateProjectRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProjectService {

    ProjectDTO createProject(Long clientId, CreateProjectRequest request);

    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);

    void deleteProject(Long projectId);

    List<ProjectDTO> getAllProject();

    ProjectDTO findProjectById(Long projectId);

    List<ProjectDTO> getProjectBySkill(Long skillId);

    void assignSkillToProject (Long projectId, Long skillId);


    @Transactional
    void removeSkillFromProject(Long projectId, Long skillId);


    Page<ProjectDTO> filter(List<String> skillNames,
                            BigDecimal minRate, BigDecimal maxRate, Boolean isHourly, Pageable pageable);

    Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable);
}
