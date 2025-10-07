package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ProjectInteractionModel;
import com.freelancemarketplace.backend.recommandation.RecommendationService;
import com.freelancemarketplace.backend.repository.ProjectInteractionModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/projects/recommend", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class RecommendationProjectController {

    private final RecommendationService recommendationService;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;
    private final RestTemplate restTemplate;
    private final String embeddingServiceUrl;

    public RecommendationProjectController(RecommendationService recommendationService,
                                           ProjectInteractionModelRepository projectInteractionModelRepository,
                                           RestTemplate restTemplate,
                                           @Value("${embedding.service.url}")String embeddingServiceUrl) {
        this.recommendationService = recommendationService;
        this.projectInteractionModelRepository = projectInteractionModelRepository;
        this.restTemplate = restTemplate;
        this.embeddingServiceUrl = embeddingServiceUrl;
    }

    @GetMapping("/freelancer/{freelancerId}")
    ResponseEntity<?> recommendProjectList(@PathVariable Long freelancerId, Pageable pageable) {
        log.info("Requesting recommendations for freelancerId: {}", freelancerId);
        try {
            Page<ProjectDTO> projects = recommendationService.recommendProjects(freelancerId, pageable);
            log.debug("Received projects: {}", projects);
            if (projects == null || projects.isEmpty()) {
                log.info("No projects found for freelancerId: {}", freelancerId);
                return ResponseEntity.ok(projects);
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


    @PostMapping("/train-cf")
    public ResponseEntity<String> trainCF() {
        List<ProjectInteractionModel> interactions = projectInteractionModelRepository.findAll();
        if (interactions.isEmpty()) {
            return ResponseEntity.ok("No interactions to train CF model");
        }

        List<Map<String, Object>> data = interactions.stream()
                .map(i -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("freelancerId", i.getFreelancer().getFreelancerId());
                    map.put("projectId", i.getProject().getProjectId());
                    map.put("score", i.getInteractionScore());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, List<Map<String, Object>>> request = Map.of("interactions", data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, List<Map<String, Object>>>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(embeddingServiceUrl + "/train-cf", entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("Failed to train CF model: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to train CF model: " + e.getMessage());
        }
    }

}
