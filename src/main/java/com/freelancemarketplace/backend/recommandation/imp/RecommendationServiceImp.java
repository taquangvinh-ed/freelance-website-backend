package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectInteractionModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import com.freelancemarketplace.backend.recommandation.RecommendationService;
import com.freelancemarketplace.backend.recommandation.ScoredProject;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.ProjectInteractionModelRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RecommendationServiceImp implements RecommendationService {

    private final ProjectsRepository projectsRepository;
    private final FreelancersRepository freelancersRepository;
    private final EmbeddingService embeddingService;
    private final RestTemplate restTemplate;
    private final ProjectMapper projectMapper;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;

    @Override
    public Page<ProjectDTO> recommendProjects(Long freelancerId, Pageable pageable) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );


        List<ProjectModel> projects = projectsRepository.findAll();


        // 1. CONTENT-BASED MATCHING (Tính điểm và Sắp xếp)
        List<ScoredProject> scoredProjectList = projects.stream()
                .map(p -> new ScoredProject(p, calculateSkillSimilarity(freelancer, p)))
                .sorted((sp1, sp2) -> Double.compare(sp2.getScore(), sp1.getScore())) // SẮP XẾP giảm dần
                .collect(Collectors.toList());

        // Tạo danh sách ProjectModel đã sắp xếp theo Content-Based
        List<ProjectModel> contentRecommendations = scoredProjectList.stream()
                .map(ScoredProject::getProject)
                .collect(Collectors.toList());

        // 2. COLLABORATIVE FILTERING
        List<ProjectModel> cfRecommendations = getCFRecommendations(freelancerId);

        // 3. HYBRID (KẾT HỢP VÀ BỎ TRÙNG LẶP)
        List<ProjectModel> hybrid = combineRecommendations(contentRecommendations, cfRecommendations);

        // 4. PHÂN TRANG
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), hybrid.size());
        List<ProjectModel> pagedModels;


        if (start >= hybrid.size()) {
            pagedModels = List.of();
        } else {
            pagedModels = hybrid.subList(start, end);
        }

        List<ProjectDTO> pagedDtos = projectMapper.toDTOs(pagedModels);


        return new PageImpl<>(pagedDtos, pageable, hybrid.size());

    }


    private List<ProjectModel> getCFRecommendations(Long freelancerId) {
        List<ProjectInteractionModel> interactions = projectInteractionModelRepository.findAll(); // Export all
        List<Map<String, Object>> interactionData = interactions.stream()
                .map(i -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("freelancerId", i.getFreelancer().getFreelancerId());
                    map.put("projectId", i.getProject().getProjectId());
                    map.put("score", i.getInteractionScore());
                    return map;
                })
                .collect(Collectors.toList());

        String url = "http://localhost:5000/cf-recommend";
        Map<String, Object> request = new HashMap<>();
        request.put("freelancerId", freelancerId);
        request.put("n", 10); // Số lượng
        request.put("allProjects", projectsRepository.findAll().stream().map(ProjectModel::getProjectId).toList());

        ResponseEntity<Map<String, List<Long>>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(request, getJsonHeaders()),
                new ParameterizedTypeReference<>() {}
        );

        List<Long> recommendedIds = response.getBody().get("recommendedProjects");
        return projectsRepository.findAllById(recommendedIds);
    }


    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private List<ProjectModel> combineRecommendations(List<ProjectModel> content, List<ProjectModel> cf) {
        // Logic kết hợp (e.g., trung bình điểm, hoặc union)
        return Stream.concat(content.stream(), cf.stream()).distinct().collect(Collectors.toList());
    }

    private double calculateSkillSimilarity(FreelancerModel f, ProjectModel p) {
        String url = "http://localhost:5000/skill-similarity";
        String freelancerSkillVectorHex = bytesToHex(f.getSkillVector());
        String projectSkillsText = p.getSkills().stream()
                .map(SkillModel::getName)
                .collect(Collectors.joining(", "));

        if (freelancerSkillVectorHex.isEmpty() || projectSkillsText.isEmpty()) {
            return 0.0;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("freelancer_skill_vector", freelancerSkillVectorHex);
        request.put("project_skills", projectSkillsText);


        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        ResponseEntity<Map<String, Double>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Double similarity = response.getBody().get("similarity");
            return similarity != null ? similarity : 0.0;
        }
        return 0.0;
    }

    private String bytesToHex(byte[] bytes) {
        return bytes != null ? new BigInteger(1, bytes).toString(16) : "";
    }
}

