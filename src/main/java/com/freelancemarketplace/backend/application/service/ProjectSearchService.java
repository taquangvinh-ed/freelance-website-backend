package com.freelancemarketplace.backend.application.service;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Application Service: Project Search & Filtering (SRP - Single Responsibility Principle)
 * This service handles ONLY search, filter, and query operations
 */
public interface ProjectSearchService {
    
    /**
     * Search projects with multiple filters
     * @param keyword Search keyword
     * @param skillNames Skill names to filter by
     * @param minRate Minimum rate
     * @param maxRate Maximum rate
     * @param isHourly Whether to filter hourly projects
     * @param duration Project duration filter
     * @param level Experience level filter
     * @param workload Workload filter
     * @param pageable Pagination info
     * @return Paginated project results
     */
    Page<ProjectDTO> searchProjects(
            String keyword,
            List<String> skillNames,
            BigDecimal minRate,
            BigDecimal maxRate,
            Boolean isHourly,
            String duration,
            String level,
            String workload,
            Pageable pageable
    );
    
    /**
     * Autocomplete search
     * @param keyword Partial keyword
     * @param limit Max results
     * @param pageable Pagination info
     * @return Paginated project results
     */
    Page<ProjectDTO> autocompleteSearch(String keyword, int limit, Pageable pageable);
    
    /**
     * Get all projects
     * @return List of all projects
     */
    List<ProjectDTO> getAllProjects();
    
    /**
     * Get projects by skill
     * @param skillId Skill ID
     * @return List of projects with that skill
     */
    List<ProjectDTO> getProjectsBySkill(Long skillId);
}

