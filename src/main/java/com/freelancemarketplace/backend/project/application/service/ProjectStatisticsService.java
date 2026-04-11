package com.freelancemarketplace.backend.project.application.service;

/**
 * Application Service: Project Statistics (SRP - Single Responsibility Principle)
 * This service handles ONLY statistics and metrics related to projects
 */
public interface ProjectStatisticsService {
    
    /**
     * Count all projects
     * @return Total project count
     */
    long countAllProjects();
    
    /**
     * Get new projects created today
     * @return Count of projects created today
     */
    long getNewProjectCountToday();
    
    /**
     * Get new projects created this week
     * @return Count of projects created this week
     */
    long getNewProjectCountWeekly();
    
    /**
     * Get count of active projects
     * @return Count of active projects
     */
    long getActiveProjectCount();
    
    /**
     * Get count of completed projects
     * @return Count of completed projects
     */
    long getCompletedProjectCount();
}

