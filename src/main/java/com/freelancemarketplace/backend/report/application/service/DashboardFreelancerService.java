package com.freelancemarketplace.backend.report.application.service;


import java.util.List;
import com.freelancemarketplace.backend.freelancer.dto.MonthlyEarningsDTO;
import com.freelancemarketplace.backend.freelancer.dto.RecentClientDTO;
import com.freelancemarketplace.backend.project.dto.DashboardProjectResponse;
import com.freelancemarketplace.backend.recommendation.dto.OverallStatsDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDistributionDTO;

public interface DashboardFreelancerService {

    List<MonthlyEarningsDTO> getMonthlyEarnings(Long freelancerId, int month);

    OverallStatsDTO getOverallStats(Long freelancerId);

    List<SkillDistributionDTO> getSkillDistribution(Long freelancerId);

    List<RecentClientDTO> getRencentClients(Long freelancerId);

    List<DashboardProjectResponse> getAllActivedProjects(Long freelancerId);

    List<DashboardProjectResponse> getAllCancelledProjects(Long freelancerId);

    List<DashboardProjectResponse> getAllCompletedProjects(Long freelancerId);
}
