package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.MonthlyEarningsDTO;
import com.freelancemarketplace.backend.dto.OverallStatsDTO;
import com.freelancemarketplace.backend.dto.RecentClientDTO;
import com.freelancemarketplace.backend.dto.SkillDistributionDTO;
import com.freelancemarketplace.backend.service.DashboardFreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/api/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardFreelancerService dashboardFreelancerService;

    @GetMapping("/freelancer/monthly-earnings")
    public ResponseEntity<List<MonthlyEarningsDTO>> getMonthlyEarnings(@AuthenticationPrincipal AppUser appUser,
                                                                       @RequestParam(value = "months", defaultValue = "6") int months) {
        Long userId = appUser.getId();
        List<MonthlyEarningsDTO> earnings = dashboardFreelancerService.getMonthlyEarnings(userId, months);
        return ResponseEntity.ok(earnings);
    }

    @GetMapping("/freelancer/stats")
    public ResponseEntity<OverallStatsDTO> getOverallStats(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        OverallStatsDTO stats = dashboardFreelancerService.getOverallStats(userId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/freelancer/skillDistribution")
    public ResponseEntity<List<SkillDistributionDTO>> getSkillDistribution(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<SkillDistributionDTO> skillDistributions = dashboardFreelancerService.getSkillDistribution(userId);
        return ResponseEntity.ok(skillDistributions);
    }


    @GetMapping("/freelancer/recentClient")
    public ResponseEntity<List<RecentClientDTO>> getRecentClients(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<RecentClientDTO> recentClients = dashboardFreelancerService.getRencentClients(userId);
        return ResponseEntity.ok(recentClients);
    }

}
