package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDashboardStatsDTO;
import com.freelancemarketplace.backend.dto.PostedProject;
import com.freelancemarketplace.backend.dto.ProjectTrackingDTO;
import com.freelancemarketplace.backend.dto.RecentPaymentDTO;

import java.util.List;

public interface DashboardClientService {
    ClientDashboardStatsDTO getStats(Long clientId);

    List<ProjectTrackingDTO> getClientActiveProjects(Long clientId);

    List<RecentPaymentDTO> getRecentPayments(Long clientId);

    List<PostedProject> getAllPostedProject(Long clientId);
}
