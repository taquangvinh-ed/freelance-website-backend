package com.freelancemarketplace.backend.report.application.service;

import com.freelancemarketplace.backend.client.dto.ClientDashboardStatsDTO;
import com.freelancemarketplace.backend.project.dto.PostedProject;
import com.freelancemarketplace.backend.recommendation.dto.ProjectTrackingDTO;
import com.freelancemarketplace.backend.payment.dto.RecentPaymentDTO;

import java.util.List;

public interface DashboardClientService {
    ClientDashboardStatsDTO getStats(Long clientId);

    List<ProjectTrackingDTO> getClientActiveProjects(Long clientId);

    List<RecentPaymentDTO> getRecentPayments(Long clientId);

    List<PostedProject> getAllPostedProject(Long clientId);
}
