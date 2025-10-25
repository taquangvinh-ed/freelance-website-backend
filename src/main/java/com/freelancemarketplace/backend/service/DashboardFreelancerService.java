package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.MonthlyEarningsDTO;
import com.freelancemarketplace.backend.dto.OverallStatsDTO;
import com.freelancemarketplace.backend.dto.RecentClientDTO;
import com.freelancemarketplace.backend.dto.SkillDistributionDTO;

import java.util.List;

public interface DashboardFreelancerService {

    List<MonthlyEarningsDTO> getMonthlyEarnings(Long freelancerId, int month);

    OverallStatsDTO getOverallStats(Long freelancerId);

    List<SkillDistributionDTO> getSkillDistribution(Long freelancerId);

    List<RecentClientDTO> getRencentClients(Long freelancerId);
}
