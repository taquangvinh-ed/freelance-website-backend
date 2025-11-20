package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.dto.ProjectDTO;
import com.freelancemarketplace.backend.dto.RecommendFreelancerDTO;
import com.freelancemarketplace.backend.enums.InvitationStatus;
import com.freelancemarketplace.backend.enums.ProjectStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.ProjectMapper;
import com.freelancemarketplace.backend.model.*;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import com.freelancemarketplace.backend.recommandation.RecommendationService;
import com.freelancemarketplace.backend.recommandation.ScoredFreelancer;
import com.freelancemarketplace.backend.recommandation.ScoredProject;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.InvitationRepository;
import com.freelancemarketplace.backend.repository.ProjectInteractionModelRepository;
import com.freelancemarketplace.backend.repository.ProjectsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class RecommendationServiceImp implements RecommendationService {

    private final ProjectsRepository projectsRepository;
    private final FreelancersRepository freelancersRepository;
    private final EmbeddingService embeddingService;
    private final RestTemplate restTemplate;
    private final ProjectMapper projectMapper;
    private final ProjectInteractionModelRepository projectInteractionModelRepository;
    private final InvitationRepository invitationRepository;

    @Override
    public Page<ProjectDTO> recommendProjects(Long freelancerId, Pageable pageable) {

        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );


        List<ProjectModel> projects = projectsRepository.findByStatus(ProjectStatus.OPEN);


        // 1. CONTENT-BASED MATCHING (Tính điểm và Sắp xếp)
        List<ScoredProject> scoredProjectList = projects.stream()
                .map(p -> new ScoredProject(p, calculateSkillSimilarity(freelancer, p)))
                .sorted((sp1, sp2) -> Double.compare(sp2.getScore(), sp1.getScore())) // SẮP XẾP giảm dần
                .toList();

        // Tạo danh sách ProjectModel đã sắp xếp theo Content-Based
        List<ProjectModel> contentRecommendations = scoredProjectList.stream()
                .map(ScoredProject::getProject)
                .collect(Collectors.toList());

        // 2. COLLABORATIVE FILTERING
        List<ProjectModel> cfRecommendations = getCFRecommendations(freelancerId);

        List<ProjectModel> cfRecommendationsOpen = cfRecommendations.stream()
                .filter(p -> p.getStatus() == ProjectStatus.OPEN)
                .toList();

        // 3. HYBRID (KẾT HỢP VÀ BỎ TRÙNG LẶP)
        List<ProjectModel> hybrid = combineRecommendations(contentRecommendations, cfRecommendationsOpen);

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

    @Override
    public Page<RecommendFreelancerDTO> recommendFreelancers(Long projectId, Pageable pageable) {
        ProjectModel project = projectsRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        List<FreelancerModel> allFreelancers = freelancersRepository.findAll();

        // === 1. LỌC THEO BUDGET (nếu là HOURLY) ===
        List<FreelancerModel> candidates = allFreelancers.stream()
                .filter(f -> !hasApplied(f, project)) // loại đã apply
                .filter(f -> isFreelancerInBudget(f, project.getBudget())) // lọc theo budget
                .toList();

        // === 2. CONTENT-BASED: skill + rating ===
        List<ScoredFreelancer> scored = candidates.stream()
                .map(f -> new ScoredFreelancer(f, calculateProjectToFreelancerSimilarity(project, f)))
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .toList();

        List<FreelancerModel> contentRecs = scored.stream()
                .map(ScoredFreelancer::getFreelancer)
                .toList();

        // === 3. COLLABORATIVE FILTERING ===
        List<FreelancerModel> cfRecs = getCFFreelancerRecommendations(projectId);

        // === 4. HYBRID ===
        List<FreelancerModel> hybrid = Stream.concat(contentRecs.stream(), cfRecs.stream())
                .distinct()
                .collect(Collectors.toList());

        // === 5. PHÂN TRANG ===
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), hybrid.size());
        List<FreelancerModel> paged = start >= hybrid.size() ? List.of() : hybrid.subList(start, end);

        List<RecommendFreelancerDTO> freelancerList = paged.stream().map(freelancerModel -> {
            RecommendFreelancerDTO recommendedFreelancer = new RecommendFreelancerDTO();
            recommendedFreelancer.setFreelancerId(freelancerModel.getFreelancerId());
            recommendedFreelancer.setFirstName(freelancerModel.getFirstName());
            recommendedFreelancer.setLastName(freelancerModel.getLastName());
            double ratingScored = calculateFreelancerRating(freelancerModel);
            recommendedFreelancer.setRating(ratingScored);
            if (freelancerModel.getBio() != null)
                recommendedFreelancer.setDescription(freelancerModel.getBio().getSummary());
            if (freelancerModel.getHourlyRate() != null)
                recommendedFreelancer.setHourlyRate(freelancerModel.getHourlyRate());
            if (freelancerModel.getAvatar() != null)
                recommendedFreelancer.setAvatar(freelancerModel.getAvatar());
            if (freelancerModel.getTitle() != null)
                recommendedFreelancer.setTitle(freelancerModel.getTitle());
            if(freelancerModel.getUser() != null){
                recommendedFreelancer.setEmail(freelancerModel.getUser().getEmail());
            }else{
                recommendedFreelancer.setEmail("freelancerhub@gmail.com");
            }
            if (invitationRepository.existsByFreelancer(freelancerModel)){
                recommendedFreelancer.setInvitation(InvitationStatus.SENT.toString());
            }else{
                recommendedFreelancer.setInvitation(InvitationStatus.NOT_SENT.toString());
            }
            return recommendedFreelancer;
        }).toList();


        return new PageImpl<>(freelancerList, pageable, hybrid.size());
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
                new ParameterizedTypeReference<>() {
                }
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

    private boolean isFreelancerInBudget(FreelancerModel freelancer, BudgetModel budget) {
        if (budget == null || freelancer.getHourlyRate() == null) {
            return false;
        }

        return switch (budget.getType()) {
            case HOURLY_RATE -> {
                BigDecimal rate = BigDecimal.valueOf(freelancer.getHourlyRate());
                BigDecimal min = budget.getMinValue();
                BigDecimal max = budget.getMaxValue();
                // Cả min và max phải có
                if (min != null && max != null) {
                    yield rate.compareTo(min) >= 0 && rate.compareTo(max) <= 0;
                } else if (min != null) {
                    yield rate.compareTo(min) >= 0;
                } else if (max != null) {
                    yield rate.compareTo(max) <= 0;
                } else {
                    yield true; // không giới hạn
                }
            }
            case FIXED_PRICE -> true; // không lọc theo hourly rate
        };
    }


    private boolean hasApplied(FreelancerModel f, ProjectModel p) {
        return f.getProposals().stream()
                .anyMatch(prop -> prop.getProject().getProjectId().equals(p.getProjectId()));
    }


    private double calculateProjectToFreelancerSimilarity(ProjectModel project, FreelancerModel freelancer) {
        double skillScore = 0.0;
        String projectVectorHex = bytesToHex(project.getSkillVector());
        String freelancerSkillsText = freelancer.getSkills().stream()
                .map(SkillModel::getName)
                .collect(Collectors.joining(", "));

        if (!projectVectorHex.isEmpty() && !freelancerSkillsText.isEmpty()) {
            skillScore = callProjectSkillSimilarity(projectVectorHex, freelancerSkillsText);
        }

        double rating = calculateFreelancerRating(freelancer);
        double ratingScore = rating / 5.0;

        return 0.8 * skillScore + 0.2 * ratingScore;
    }


    private double calculateFreelancerRating(FreelancerModel f) {
        return f.getTestimonials().stream()
                .filter(t -> t.getRatingScore() != null)
                .mapToInt(TestimonialModel::getRatingScore)
                .average()
                .orElse(0.0);
    }


    private double callProjectSkillSimilarity(String projectSkillVectorHex, String freelancerSkillsText) {
        String url = "http://localhost:5000/project-skill-similarity";

        Map<String, String> request = new HashMap<>();
        request.put("project_skill_vector", projectSkillVectorHex);
        request.put("freelancer_skills", freelancerSkillsText);

        HttpHeaders headers = getJsonHeaders();
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, Double>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Double>>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Double similarity = response.getBody().get("similarity");
                return similarity != null ? similarity : 0.0;
            }
        } catch (Exception e) {
            log.warn("Failed to call /project-skill-similarity: {}", e.getMessage());
        }

        return 0.0;
    }


    private List<FreelancerModel> getCFFreelancerRecommendations(Long projectId) {
        String url = "http://localhost:5000/cf-recommend-freelancers";

        // Lấy danh sách tất cả freelancer ID
        List<Long> allFreelancerIds = freelancersRepository.findAll()
                .stream()
                .map(FreelancerModel::getFreelancerId)
                .collect(Collectors.toList());

        // Tạo request body
        Map<String, Object> request = new HashMap<>();
        request.put("projectId", projectId);
        request.put("n", 10); // gợi ý tối đa 10 người
        request.put("allFreelancers", allFreelancerIds);

        HttpHeaders headers = getJsonHeaders();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<Map<String, List<Long>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, List<Long>>>() {
                    }
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Long> recommendedIds = response.getBody().get("recommendedFreelancers");
                if (recommendedIds != null && !recommendedIds.isEmpty()) {
                    return freelancersRepository.findAllById(recommendedIds);
                }
            }
        } catch (Exception e) {
            log.error("CF recommendation FAILED for project {}: {}", projectId, e.toString(), e);
            throw new RuntimeException("Collaborative filtering service unavailable", e);
        }

        return List.of(); // trả về rỗng nếu lỗi
    }

}

