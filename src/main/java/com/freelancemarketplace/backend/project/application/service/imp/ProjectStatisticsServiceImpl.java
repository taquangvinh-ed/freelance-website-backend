package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.project.application.port.ProjectCrudPort;
import com.freelancemarketplace.backend.project.application.service.ProjectStatisticsService;
import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * Service Implementation: Project Statistics (SRP)
 * 
 * Demonstrates:
 * - SRP: Only handles statistics calculations
 * - DIP: Depends on ProjectCrudPort (abstraction)
 * - No mixing with CRUD or search logic
 */
@Service
@AllArgsConstructor
@Slf4j
public class ProjectStatisticsServiceImpl implements ProjectStatisticsService {
    
    private final ProjectCrudPort projectCrudPort;
    private final ProjectsRepository projectsRepository;
    
    @Override
    public long countAllProjects() {
        log.info("Counting all projects");
        return projectCrudPort.count();
    }
    
    @Override
    public long getNewProjectCountToday() {
        log.info("Getting new projects created today");

        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);

        return projectsRepository.countByCreatedAtBetween(startOfToday, endOfToday);
    }
    
    @Override
    public long getNewProjectCountWeekly() {
        log.info("Getting new projects created this week");

        LocalDateTime startOfWeek = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                .toLocalDate()
                .atStartOfDay();

        LocalDateTime endOfWeek = startOfWeek.plusDays(7);

        return projectsRepository.countByCreatedAtBetween(startOfWeek, endOfWeek);
    }
    
    @Override
    public long getActiveProjectCount() {
        log.info("Getting count of active projects");
        return projectCrudPort.countActiveProjects();
    }
    
    @Override
    public long getCompletedProjectCount() {
        log.info("Getting count of completed projects");
        return projectsRepository.findByStatus(ProjectStatus.COMPLETED).size();
    }
}
