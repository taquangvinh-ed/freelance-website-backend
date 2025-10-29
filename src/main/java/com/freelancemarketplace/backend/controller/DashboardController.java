package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.service.DashboardClientService;
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
    private final DashboardClientService dashboardClientService;

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

    @GetMapping("/freelancer/projects/activatedProjects")
    ResponseEntity<List<DashboardProjectResponse>> getALlActivatedProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> activatedProjects = dashboardFreelancerService.getAllActivedProjects(userId);
        return ResponseEntity.ok(activatedProjects);
    }

    @GetMapping("/freelancer/projects/cancelledProjects")
    ResponseEntity<List<DashboardProjectResponse>> getAllCancelledProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> cancelledProjects = dashboardFreelancerService.getAllCancelledProjects(userId);
        return ResponseEntity.ok(cancelledProjects);
    }

    @GetMapping("/freelancer/projects/completedProjects")
    ResponseEntity<List<DashboardProjectResponse>> getAllCompletedProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> completedProjects = dashboardFreelancerService.getAllCompletedProjects(userId);
        return ResponseEntity.ok(completedProjects);
    }


    @GetMapping("/client/getStats")
    ResponseEntity<ClientDashboardStatsDTO> getStats(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        ClientDashboardStatsDTO result = dashboardClientService.getStats(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/trackingProjects")
    ResponseEntity<List<ProjectTrackingDTO>>trackProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<ProjectTrackingDTO> list = dashboardClientService.getClientActiveProjects(userId);
        return ResponseEntity.ok(list);
    }

}
