package com.freelancemarketplace.backend.project.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.api.request.CreateProjectRequest;
import com.freelancemarketplace.backend.project.application.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/projects", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/")
    public ApiResponse<?> createProject(@AuthenticationPrincipal AppUser appUser, @RequestBody CreateProjectRequest req){

        Long userId = appUser.getId();

        ProjectDTO newProject = projectService.createProject(userId, req);
        return ApiResponse.created(newProject);
    }

    @PutMapping("/{projectId}")
    ApiResponse<?> updateProject(@PathVariable Long projectId,
                                             @RequestBody ProjectDTO projectDTO){
        ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);
        return ApiResponse.success(updatedProject);

    }

    @DeleteMapping("/{projectId}")
    public ApiResponse<?> deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getAllProjects")
    public ApiResponse<?> getAllProject(){
        List<ProjectDTO> projects = projectService.getAllProject();
        return ApiResponse.success(projects);
    }

    @GetMapping("/findProject/{projectId}")
    public ApiResponse<?> findProjectById(@PathVariable Long projectId){
        ProjectDTO project = projectService.findProjectById(projectId);
        return ApiResponse.success(project);
    }

    @PutMapping("/assignSkillToProject/Project/{projectId}/Skill/{skillId}")
    public ApiResponse<?> assignSkillToProject(@PathVariable Long projectId,
                                                           @PathVariable Long skillId){
        projectService.assignSkillToProject(projectId,skillId);
        return ApiResponse.noContent();

    }

    @PutMapping("/removeSkillFromProject/Project/{projectId}/Skill/{skillId}")
    public ApiResponse<?> removeSkillFromProject(@PathVariable Long projectId,
                                                           @PathVariable Long skillId){
        projectService.removeSkillFromProject(projectId,skillId);
        return ApiResponse.noContent();

    }

    @GetMapping("/filter")
    public ApiResponse<?> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> skillNames,
            @RequestParam(required = false) BigDecimal minRate,
            @RequestParam(required = false) BigDecimal maxRate,
            @RequestParam(required = false) Boolean isHourly,
            @RequestParam(required = false) String duration,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String workload,
            Pageable pageable) {

        Page<ProjectDTO> projects = projectService.filter(keyword,
                skillNames, minRate, maxRate, isHourly, duration, level, workload, pageable);
        return ApiResponse.success(projects);
    }

    @GetMapping("/autocomplete-search")
    public ApiResponse<?> autocompleteSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit, Pageable pageable) {

        Page<ProjectDTO> projects = projectService.autocompleteSearch(keyword, limit, pageable);
        return ApiResponse.success(projects);
    }

}
