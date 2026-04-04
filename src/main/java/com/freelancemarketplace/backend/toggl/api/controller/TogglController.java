package com.freelancemarketplace.backend.toggl.api.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.toggl.dto.ClockifyTimeEntryResponse;
import com.freelancemarketplace.backend.toggl.dto.TogglTimeEntryResponseDTO;
import com.freelancemarketplace.backend.toggl.application.service.TogglService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/toggl/tracker", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TogglController {

    private final TogglService togglService;


    @PostMapping("/create-project")
    ApiResponse<?> createProjectOnToggl(@RequestParam String projectName){
        Long togglProjectId = togglService.createProjectOnToggl(projectName);
        return ApiResponse.success(togglProjectId);
    }


    @PostMapping("/start/{contractId}")
    public ApiResponse<?> startTimer(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable Long contractId,
            @RequestParam(required = false) String description) {

        Long freelancerId = appUser.getId();

        TogglTimeEntryResponseDTO response = togglService.startTimeEntry(
                freelancerId,
                contractId,
                description
        );


        return ApiResponse.success(response);
    }

    @PostMapping("/stop/{contractId}")
    public ApiResponse<?> stopTimer(
            @AuthenticationPrincipal AppUser appUser, @PathVariable Long contractId) {

        Long freelancerId = appUser.getId();

        TogglTimeEntryResponseDTO response = togglService.stopTimeEntry(freelancerId, contractId);

        return ApiResponse.success(response);
    }

}
