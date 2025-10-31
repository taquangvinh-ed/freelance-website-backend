package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ExperienceDTO;
import com.freelancemarketplace.backend.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/experiences", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping("/")
    ResponseEntity<ExperienceDTO>create(@AuthenticationPrincipal AppUser appUser, @RequestBody ExperienceDTO experienceDTO){
        Long freelancerId = appUser.getId();
        ExperienceDTO newExperience = experienceService.create(freelancerId, experienceDTO);
        return ResponseEntity.ok(newExperience);
    }

    @PatchMapping("/{experienceId}")
    ResponseEntity<ExperienceDTO>update(@PathVariable Long experienceId, @RequestBody ExperienceDTO experienceDTO){
        ExperienceDTO updatedExperience = experienceService.update(experienceId, experienceDTO);
        return ResponseEntity.ok(updatedExperience);
    }


    @DeleteMapping("/{experienceId}")
    ResponseEntity<Void> delete(@PathVariable Long experienceId){
        experienceService.delete(experienceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getByFreelancer")
    ResponseEntity<List<ExperienceDTO>> getAllByFreelancer(@AuthenticationPrincipal AppUser appUser){
        Long freelancerId = appUser.getId();
        List<ExperienceDTO> experiences = experienceService.getAllExperienceByFreelancer(freelancerId);
        return ResponseEntity.ok(experiences);
    }
}
