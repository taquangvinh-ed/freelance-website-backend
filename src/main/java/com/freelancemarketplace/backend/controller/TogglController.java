package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ClockifyTimeEntryResponse;
import com.freelancemarketplace.backend.dto.TogglTimeEntryResponseDTO;
import com.freelancemarketplace.backend.service.TogglService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/toggl/tracker", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class TogglController {

    private final TogglService togglService;


    @PostMapping("/create-project")
    ResponseEntity<Long>createProjectOnToggl(@RequestParam String projectName){
        Long togglProjectId = togglService.createProjectOnToggl(projectName);
        return ResponseEntity.ok(togglProjectId);
    }


    @PostMapping("/start/{contractId}")
    public ResponseEntity<TogglTimeEntryResponseDTO> startTimer(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable Long contractId,
            @RequestParam(required = false) String description) {

        Long freelancerId = appUser.getId();

        TogglTimeEntryResponseDTO response = togglService.startTimeEntry(
                freelancerId,
                contractId,
                description
        );


        return ResponseEntity.ok(response);
    }

    @PostMapping("/stop/{contractId}")
    public ResponseEntity<TogglTimeEntryResponseDTO> stopTimer(
            @AuthenticationPrincipal AppUser appUser, @PathVariable Long contractId) {

        Long freelancerId = appUser.getId();

        TogglTimeEntryResponseDTO response = togglService.stopTimeEntry(freelancerId, contractId);

        return ResponseEntity.ok(response);
    }

}
