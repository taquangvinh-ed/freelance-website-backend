package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.domain.enums.UserRoles;
import com.freelancemarketplace.backend.service.ProjectService;
import com.freelancemarketplace.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/admin/dashboard", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserService userService;
    private final ProjectService projectService;


    @GetMapping("/count-user-by-role")
    public ApiResponse<?> countUserByRole(@RequestParam UserRoles role){
        long count = userService.countAllUserByRole(role);
        return ApiResponse.success(count);
    }

    @GetMapping("/count-new-freelancer-weekly")
    public ApiResponse<?> getNewFreelancerCountWeekly(){
        long count = userService.getNewFreelancerCountWeekly();
        return ApiResponse.success(count);
    }

    @GetMapping("/count-new-freelancer-monthly")
    public ApiResponse<?> getNewFreelancerCountMonthly(){
        long count = userService.getNewFreelancerCountMonthly();
        return ApiResponse.success(count);
    }

    @GetMapping("/count-new-client-weekly")
    public ApiResponse<?> getNewClientCountWeekly(){
        long count = userService.getNewClientCountWeekly();
        return ApiResponse.success(count);
    }

    @GetMapping("/count-new-client-monthly")
    public ApiResponse<?> getNewClientCountMonthly(){
        long count = userService.getNewClientCountMonthly();
        return ApiResponse.success(count);
    }

    @GetMapping("/count-all-projects")
    public ApiResponse<?> countAllProjects(){
        long countProject = projectService.countAllProjects();
        return ApiResponse.success(countProject);
    }

    @GetMapping("/count-all-projects-today")
    public ApiResponse<?> countAllProjectsToday(){
        long countProject = projectService.getNewProjectCountToday();
        return ApiResponse.success(countProject);
    }

    @GetMapping("/count-all-projects-this-week")
    public ApiResponse<?> countAllProjectsWeekly(){
        long countProject = projectService.getNewProjectCountWeekly();
        return ApiResponse.success(countProject);
    }

    @GetMapping("/count-all-projects-active")
    public ApiResponse<?> countAllProjectsInActive(){
        long countProject = projectService.getActiveProjectCount();
        return ApiResponse.success(countProject);
    }

    @GetMapping("/count-all-projects-completed")
    public ApiResponse<?> countAllCompletedProjects(){
        long countProject = projectService.getCompletedProjectCount();
        return ApiResponse.success(countProject);
    }
}
