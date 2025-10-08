package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDTO>createProject(@RequestBody ProjectDTO projectDTO){
        ProjectDTO newProject = projectService.createProject(projectDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newProject
                ));
    }

    @PutMapping("/{projectId}")
    ResponseEntity<ResponseDTO>updateProject(@PathVariable Long projectId,
                                             @RequestBody ProjectDTO projectDTO){
        ProjectDTO updatedProject = projectService.updateProject(projectId, projectDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedProject
                ));


    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ResponseDTO>deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));
    }

    @GetMapping("/getAllProjects")
    public ResponseEntity<ResponseDTO>getAllProject(){
        List<ProjectDTO> projects = projectService.getAllProject();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        projects
                ));
    }

    @GetMapping("/findProject/{projectId}")
    public ResponseEntity<ResponseDTO>findProjectById(@PathVariable Long projectId){
        ProjectDTO project = projectService.findProjectById(projectId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        project
                ));
    }


    @PutMapping("/assignSkillToProject/Project/{projectId}/Skill/{skillId}")
    public ResponseEntity<ResponseDTO>assignSkillToProject(@PathVariable Long projectId,
                                                           @PathVariable Long skillId){
        projectService.assignSkillToProject(projectId,skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS
        ));

    }

    @PutMapping("/removeSkillFromProject/Project/{projectId}/Skill/{skillId}")
    public ResponseEntity<ResponseDTO>removeSkillFromProject(@PathVariable Long projectId,
                                                           @PathVariable Long skillId){
        projectService.removeSkillFromProject(projectId,skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));

    }

    @GetMapping("/filter")
    public Page<ProjectDTO> advancedSearch(
            @RequestParam(required = false) List<String> skillNames,
            @RequestParam(required = false) BigDecimal minRate,
            @RequestParam(required = false) BigDecimal maxRate,
            @RequestParam(required = false) Boolean isHourly,
            Pageable pageable) {


        return projectService.filter(
                skillNames, minRate, maxRate, isHourly, pageable);
    }

    @GetMapping("/autocomplete-search")
    public Page<ProjectDTO> autocompleteSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "10") int limit, Pageable pageable) {

        return projectService.autocompleteSearch(keyword, limit, pageable);
    }

}
