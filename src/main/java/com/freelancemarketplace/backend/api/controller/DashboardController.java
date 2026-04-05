package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.report.application.service.DashboardClientService;
import com.freelancemarketplace.backend.report.application.service.DashboardFreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.freelancemarketplace.backend.client.dto.ClientDashboardStatsDTO;
import com.freelancemarketplace.backend.freelancer.dto.MonthlyEarningsDTO;
import com.freelancemarketplace.backend.freelancer.dto.RecentClientDTO;
import com.freelancemarketplace.backend.payment.dto.RecentPaymentDTO;
import com.freelancemarketplace.backend.project.dto.DashboardProjectResponse;
import com.freelancemarketplace.backend.project.dto.PostedProject;
import com.freelancemarketplace.backend.recommendation.dto.OverallStatsDTO;
import com.freelancemarketplace.backend.recommendation.dto.ProjectTrackingDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDistributionDTO;

@RestController
@RequestMapping(path = "/api/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardFreelancerService dashboardFreelancerService;
    private final DashboardClientService dashboardClientService;

    @GetMapping("/freelancer/monthly-earnings")
    public ApiResponse<?> getMonthlyEarnings(@AuthenticationPrincipal AppUser appUser,
                                                                       @RequestParam(value = "months", defaultValue = "6") int months) {
        Long userId = appUser.getId();
        List<MonthlyEarningsDTO> earnings = dashboardFreelancerService.getMonthlyEarnings(userId, months);
        return ApiResponse.success(earnings);
    }

    @GetMapping("/freelancer/stats")
    public ApiResponse<?> getOverallStats(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        OverallStatsDTO stats = dashboardFreelancerService.getOverallStats(userId);
        return ApiResponse.success(stats);
    }

    @GetMapping("/freelancer/skillDistribution")
    public ApiResponse<?> getSkillDistribution(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<SkillDistributionDTO> skillDistributions = dashboardFreelancerService.getSkillDistribution(userId);
        return ApiResponse.success(skillDistributions);
    }

    @GetMapping("/freelancer/recentClient")
    public ApiResponse<?> getRecentClients(
            @AuthenticationPrincipal AppUser appUser) {
        Long userId = appUser.getId();
        List<RecentClientDTO> recentClients = dashboardFreelancerService.getRencentClients(userId);
        return ApiResponse.success(recentClients);
    }

    @GetMapping("/freelancer/projects/activatedProjects")
    public ApiResponse<?> getALlActivatedProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> activatedProjects = dashboardFreelancerService.getAllActivedProjects(userId);
        return ApiResponse.success(activatedProjects);
    }

    @GetMapping("/freelancer/projects/cancelledProjects")
    public ApiResponse<?> getAllCancelledProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> cancelledProjects = dashboardFreelancerService.getAllCancelledProjects(userId);
        return ApiResponse.success(cancelledProjects);
    }

    @GetMapping("/freelancer/projects/completedProjects")
    public ApiResponse<?> getAllCompletedProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<DashboardProjectResponse> completedProjects = dashboardFreelancerService.getAllCompletedProjects(userId);
        return ApiResponse.success(completedProjects);
    }

    @GetMapping("/client/getStats")
    public ApiResponse<?> getStats(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        ClientDashboardStatsDTO result = dashboardClientService.getStats(userId);
        return ApiResponse.success(result);
    }

    @GetMapping("/client/trackingProjects")
    public ApiResponse<?> trackProjects(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<ProjectTrackingDTO> list = dashboardClientService.getClientActiveProjects(userId);
        return ApiResponse.success(list);
    }

    @GetMapping("/client/recentPayment")
    public ApiResponse<?> getRecentPayments(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<RecentPaymentDTO> recentPayments = dashboardClientService.getRecentPayments(userId);
        return ApiResponse.success(recentPayments);
    }

    @GetMapping("/client/getAllPostedProjects")
    public ApiResponse<?> getAllPostedProject(@AuthenticationPrincipal AppUser appUser){
        Long userId = appUser.getId();
        List<PostedProject> list = dashboardClientService.getAllPostedProject(userId);
        return ApiResponse.success(list);
    }

}
