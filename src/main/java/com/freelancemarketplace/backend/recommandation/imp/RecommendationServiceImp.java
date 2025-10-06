package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.ProjectModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import com.freelancemarketplace.backend.recommandation.RecommendationService;
import com.freelancemarketplace.backend.recommandation.ScoredProject;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecommendationServiceImp implements RecommendationService {

    private final ProjectsRepository projectsRepository;
    private final FreelancersRepository freelancersRepository;
    private final EmbeddingService embeddingService;
    private final RestTemplate restTemplate;
    private final ProjectMapper projectMapper;

    @Override
    public List<ProjectDTO> recommendProjects(Long freelancerId) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );


        List<ProjectModel> projects = projectsRepository.findAll();

        List<ScoredProject> scoredProjectList = new ArrayList<>();
        for (ProjectModel p : projects) {
            double score = calculateSkillSimilarity(freelancer, p);
            scoredProjectList.add(new ScoredProject(p, score));
        }

        List<ProjectModel> recommendProjects = scoredProjectList.stream()
                .sorted((p1, p2) -> Double.compare(p2.getScore(), p1.getScore()))
                .map(ScoredProject::getProject)
                .limit(5)
                .toList();
        return projectMapper.toDTOs(recommendProjects);
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

