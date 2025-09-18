package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectMapper;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import com.freelancemarketplace.backend.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImp implements ProjectService {

    private final ProjectsRepository projectsRepository;
    private final ProjectMapper projectMapper;

    public ProjectServiceImp(ProjectsRepository projectsRepository, ProjectMapper projectMapper) {
        this.projectsRepository = projectsRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        ProjectModel newProject = projectMapper.toEntity(projectDTO);

        ProjectModel savedProject = projectsRepository.save(newProject);

        return projectMapper.toDto(savedProject);
    }

    @Override
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {

        ProjectModel project = projectsRepository.findById(projectId).orElseThrow(
                ()-> new ResourceNotFoundException("Project with id: " + projectId + " not found.")
        );

        ProjectModel updatedProject = projectMapper.partialUpdate(projectDTO, project);

        ProjectModel savedProject = projectsRepository.save(updatedProject);

        return projectMapper.toDto(savedProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Boolean isExisted = projectsRepository.existsById(projectId);
        if(!isExisted){
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
    public List<ProjectDTO> getProjectBySkill(Long skillId) {
        return List.of();
    }
}
