package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.project.application.service.ProjectSearchService;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.infrastructure.mapper.ProjectMapper;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.project.domain.specification.ProjectSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation: Project Search & Filtering (SRP)
 * 
 * Demonstrates:
 * - SRP: Only handles search and filter operations
 * - DIP: Depends on repositories through abstraction
 * - Uses Specification pattern for flexible filtering
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectSearchServiceImpl implements ProjectSearchService {
    
    private final ProjectsRepository projectsRepository;
    private final ProjectMapper projectMapper;
    
    @Override
    public Page<ProjectDTO> searchProjects(
            String keyword,
            List<String> skillNames,
            BigDecimal minRate,
            BigDecimal maxRate,
            Boolean isHourly,
            String duration,
            String level,
            String workload,
            Pageable pageable
    ) {
        log.info("Searching projects with keyword: {}, skills: {}", keyword, skillNames);
        
        // ProjectSpecification currently exposes filter(...) + autocompleteSearch(...)
        Specification<ProjectModel> spec =
                ProjectSpecification.filter(skillNames, minRate, maxRate, isHourly, duration, level, workload);

        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(ProjectSpecification.autocompleteSearch(keyword));
        }
        
        // Execute search
        Page<ProjectModel> projects = projectsRepository.findAll(spec, pageable);
        
        // Convert to DTOs
        List<ProjectDTO> dtos = projects.getContent()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
        
        log.info("Found {} projects", dtos.size());
        
        return new PageImpl<>(dtos, pageable, projects.getTotalElements());
    }
    
    @Override
    public Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable) {
        log.info("Autocompleting search for: {}", keyword);
        
        Pageable limitedPageable = PageRequest.of(0, limit);
        
        Specification<ProjectModel> spec = ProjectSpecification.autocompleteSearch(keyword);
        Page<ProjectModel> projects = projectsRepository.findAll(spec, limitedPageable);
        
        List<ProjectDTO> dtos = projects.getContent()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtos, limitedPageable, projects.getTotalElements());
    }
    
    @Override
    public List<ProjectDTO> getAllProjects() {
        log.info("Getting all projects");
        
        return projectsRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProjectDTO> getProjectsBySkill(Long skillId) {
        log.info("Getting projects with skill: {}", skillId);
        
        return projectsRepository.findBySkills_SkillId(skillId)
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }
}

