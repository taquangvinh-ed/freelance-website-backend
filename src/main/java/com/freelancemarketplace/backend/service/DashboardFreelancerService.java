package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.*;

import java.util.List;

public interface DashboardFreelancerService {

    List<MonthlyEarningsDTO> getMonthlyEarnings(Long freelancerId, int month);

    OverallStatsDTO getOverallStats(Long freelancerId);

    List<SkillDistributionDTO> getSkillDistribution(Long freelancerId);

    List<RecentClientDTO> getRencentClients(Long freelancerId);

    List<DashboardProjectResponse> getAllActivedProjects(Long freelancerId);

    List<DashboardProjectResponse> getAllCancelledProjects(Long freelancerId);

    List<DashboardProjectResponse> getAllCompletedProjects(Long freelancerId);
}
