package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.recommandation.RecommendationService;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(path = "/api/projects/recommend", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Slf4j
public class RecommendationProjectController {

    private final RecommendationService recommendationService;

    @GetMapping("/freelancer/{freelancerId}")
    ResponseEntity<?> recommendProjectList(@PathVariable Long freelancerId) {
        log.info("Requesting recommendations for freelancerId: {}", freelancerId);
        try {
            List<ProjectDTO> projects = recommendationService.recommendProjects(freelancerId);
            log.debug("Received projects: {}", projects);
            if (projects == null || projects.isEmpty()) {
                log.info("No projects found for freelancerId: {}", freelancerId);
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(projects);
        } catch (ResourceNotFoundException e) {
            log.error("Freelancer not found for id: {}", freelancerId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            log.error("Internal server error for freelancerId: {}", freelancerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage()); // Trả về thông báo lỗi
        }
    }


}
