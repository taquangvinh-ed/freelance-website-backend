package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.enums.UserRoles;
import com.freelancemarketplace.backend.service.ProjectService;
import com.freelancemarketplace.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Long> countUserByRole(@RequestParam UserRoles role){
        long count = userService.countAllUserByRole(role);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-new-freelancer-weekly")
    ResponseEntity<Long> getNewFreelancerCountWeekly(){
        long count = userService.getNewFreelancerCountWeekly();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-new-freelancer-monthly")
    ResponseEntity<Long> getNewFreelancerCountMonthly(){
        long count = userService.getNewFreelancerCountMonthly();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-new-client-weekly")
    ResponseEntity<Long> getNewClientCountWeekly(){
        long count = userService.getNewClientCountWeekly();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-new-client-monthly")
    ResponseEntity<Long> getNewClientCountMonthly(){
        long count = userService.getNewClientCountMonthly();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count-all-projects")
    ResponseEntity<Long> countAllProjects(){
        long countProject = projectService.countAllProjects();
        return ResponseEntity.ok(countProject);
    }

    @GetMapping("/count-all-projects-today")
    ResponseEntity<Long> countAllProjectsToday(){
        long countProject = projectService.getNewProjectCountToday();
        return ResponseEntity.ok(countProject);
    }

    @GetMapping("/count-all-projects-this-week")
    ResponseEntity<Long> countAllProjectsWeekly(){
        long countProject = projectService.getNewProjectCountWeekly();
        return ResponseEntity.ok(countProject);
    }

    @GetMapping("/count-all-projects-active")
    ResponseEntity<Long> countAllProjectsInActive(){
        long countProject = projectService.getActiveProjectCount();
        return ResponseEntity.ok(countProject);
    }

    @GetMapping("/count-all-projects-completed")
    ResponseEntity<Long> countAllCompletedProjects(){
        long countProject = projectService.getCompletedProjectCount();
        return ResponseEntity.ok(countProject);
    }

}
