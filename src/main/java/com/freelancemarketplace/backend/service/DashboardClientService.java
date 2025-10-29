package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDashboardStatsDTO;
import com.freelancemarketplace.backend.dto.ProjectTrackingDTO;

import java.util.List;

public interface DashboardClientService {
    ClientDashboardStatsDTO getStats(Long clientId);

    List<ProjectTrackingDTO> getClientActiveProjects(Long clientId);
}
