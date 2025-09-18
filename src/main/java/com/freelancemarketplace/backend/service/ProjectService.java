package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO createProject(ProjectDTO projectDTO);

    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);

    void deleteProject(Long projectId);

    List<ProjectDTO> getAllProject();

    List<ProjectDTO> getProjectBySkill(Long skillId);


}
