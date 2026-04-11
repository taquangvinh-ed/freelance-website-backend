package com.freelancemarketplace.backend.skill.application.service.imp;

import com.freelancemarketplace.backend.skill.application.service.ProjectSkillService;
import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.skill.infrastructure.repository.SkillsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation: Project Skill Management (SRP)
 * 
 * Demonstrates:
 * - SRP: Only handles skill assignment/removal
 * - Proper error handling with domain exceptions
 * - Transaction management for data consistency
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectSkillServiceImpl implements ProjectSkillService {
    
    private final ProjectsRepository projectsRepository;
    private final SkillsRepository skillsRepository;
    
    @Override
    @Transactional
    public void assignSkillToProject(Long projectId, Long skillId) {
        log.info("Assigning skill {} to project {}", skillId, projectId);
        
        // Find project
        ProjectModel project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.PROJECT_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Project not found with id: " + projectId
                ));
        
        // Find skill
        SkillModel skill = skillsRepository.findById(skillId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.INVALID_REQUEST,
                        HttpStatus.NOT_FOUND,
                        "Skill not found with id: " + skillId
                ));
        
        // Check if skill already assigned
        if (project.getSkills() != null && project.getSkills().contains(skill)) {
            log.warn("Skill {} already assigned to project {}", skillId, projectId);
            return;
        }
        
        // Add skill
        project.getSkills().add(skill);
        projectsRepository.save(project);
        
        log.info("Skill {} assigned to project {} successfully", skillId, projectId);
    }
    
    @Override
    @Transactional
    public void removeSkillFromProject(Long projectId, Long skillId) {
        log.info("Removing skill {} from project {}", skillId, projectId);
        
        // Find project
        ProjectModel project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.PROJECT_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Project not found with id: " + projectId
                ));
        
        // Find skill
        SkillModel skill = skillsRepository.findById(skillId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.INVALID_REQUEST,
                        HttpStatus.NOT_FOUND,
                        "Skill not found with id: " + skillId
                ));
        
        // Check if skill is assigned
        if (project.getSkills() == null || !project.getSkills().contains(skill)) {
            log.warn("Skill {} not assigned to project {}", skillId, projectId);
            return;
        }
        
        // Remove skill
        project.getSkills().remove(skill);
        projectsRepository.save(project);
        
        log.info("Skill {} removed from project {} successfully", skillId, projectId);
    }
}

