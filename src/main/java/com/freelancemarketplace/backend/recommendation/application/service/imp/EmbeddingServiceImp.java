package com.freelancemarketplace.backend.recommendation.application.service.imp;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.recommendation.application.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EmbeddingServiceImp implements EmbeddingService {

    private final RestTemplate restTemplate;

    private final String embeddingServiceUrl;

    public EmbeddingServiceImp(RestTemplate restTemplate,
                               @Value("${embedding.service.url}") String embeddingServiceUrl) {
        this.restTemplate = restTemplate;
        this.embeddingServiceUrl = embeddingServiceUrl;
    }

    @Override
    public byte[] generateEmbedding(String text) {

        String url = embeddingServiceUrl + "/generate-embeddings";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text != null ? text : "");

        // Cấu hình header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HttpEntity với body và header
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String hexEmbedding = response.getBody().get("embedding");
            if (hexEmbedding != null) {
                return hexStringToByteArray(hexEmbedding);
            }
        }

        return new byte[0];
    }


    @Override
    public byte[] generateSkillVector(FreelancerModel freelancer) {
        if (freelancer == null || freelancer.getSkills() == null || freelancer.getSkills().isEmpty()) {
            return new byte[0];
        }
        String skillsText = freelancer.getSkills().stream()
                .map(SkillModel::getName)
                .collect(Collectors.joining(", "));
        return generateEmbedding(skillsText); // Gọi chung phương thức generateEmbedding
    }

    private byte[] hexStringToByteArray(String hex) {
        if (hex == null || hex.isBlank()) {
            return new byte[0];
        }

        String normalizedHex = hex.trim();
        if ((normalizedHex.length() & 1) != 0) {
            normalizedHex = "0" + normalizedHex;
        }

        int length = normalizedHex.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            data[i / 2] = (byte) ((Character.digit(normalizedHex.charAt(i), 16) << 4)
                    + Character.digit(normalizedHex.charAt(i + 1), 16));
        }
        return data;
    }


    @Override
    public String generateSummary(FreelancerModel freelancer) {
        StringBuilder sb = new StringBuilder();
        sb.append(freelancer.getTitle() != null ? freelancer.getTitle() : "Freelancer");
        if (freelancer.getSkills() != null && !freelancer.getSkills().isEmpty()) {
            sb.append(" skilled in ").append(freelancer.getSkills().stream()
                    .map(SkillModel::getName)
                    .collect(Collectors.joining(", ")));
        }
        if (freelancer.getExperiences() != null && !freelancer.getExperiences().isEmpty()) {
            sb.append(". With experience in ").append(freelancer.getExperiences().iterator().next().getTitle());
        }
        return sb.toString();
    }

    @Override
    public byte[] generateProjectSkillVector(ProjectModel project) {
        Collection<SkillModel> skills = project == null || project.getSkills() == null ? List.of() : project.getSkills();
        String skillsText = skills.stream()
                .map(SkillModel::getName)
                .collect(Collectors.joining(", "));

        if (skillsText.isBlank()) {
            if (project != null) {
                project.setSkillVector(null);
            }
            return new byte[0];
        }

        return generateEmbedding(skillsText);
    }
}
