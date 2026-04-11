package com.freelancemarketplace.backend.recommendation.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.dto.ProjectDTO;
import com.freelancemarketplace.backend.recommendation.dto.RecommendFreelancerDTO;
import com.freelancemarketplace.backend.project.domain.model.ProjectInteractionModel;
import com.freelancemarketplace.backend.recommendation.application.service.RecommendationService;
import com.freelancemarketplace.backend.recommendation.infrastructure.repository.ProjectInteractionModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/projects/recommend", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;
    private final RestTemplate restTemplate;
    private final String embeddingServiceUrl;

    public RecommendationController(RecommendationService recommendationService,
                                    ProjectInteractionModelRepository projectInteractionModelRepository,
                                    RestTemplate restTemplate,
                                    @Value("${embedding.service.url}") String embeddingServiceUrl) {
        this.recommendationService = recommendationService;
        this.projectInteractionModelRepository = projectInteractionModelRepository;
        this.restTemplate = restTemplate;
        this.embeddingServiceUrl = embeddingServiceUrl;
    }

    @GetMapping("/freelancer")
    ApiResponse<?> recommendProjectList(@AuthenticationPrincipal AppUser principal, Pageable pageable) {
        Long freelancerId = principal.getId();
        log.info("Requesting recommendations for freelancerId: {}", freelancerId);
        Page<ProjectDTO> projects = recommendationService.recommendProjects(freelancerId, pageable);
        log.debug("Received projects: {}", projects);
        return ApiResponse.success(projects);
    }

    @GetMapping("/client/{projectId}")
    public ApiResponse<?> recommendFreelancersToProject(@PathVariable Long projectId, Pageable pageable){
        Page<RecommendFreelancerDTO> freelancers = recommendationService.recommendFreelancers(projectId, pageable);
        return ApiResponse.success(freelancers);
    }

    @PostMapping("/train-cf")
    public ApiResponse<?> trainCF() {
        List<ProjectInteractionModel> interactions = projectInteractionModelRepository.findAll();
        if (interactions.isEmpty()) {
            return ApiResponse.success("No interactions to train CF model");
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

        ResponseEntity<String> response = restTemplate.postForEntity(embeddingServiceUrl + "/train-cf", entity, String.class);
        return ApiResponse.success(response.getBody());
    }

}
