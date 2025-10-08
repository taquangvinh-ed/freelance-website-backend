package com.freelancemarketplace.backend.recommandation.imp;

import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.recommandation.EmbeddingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
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
        if (freelancer.getSkills() != null && freelancer.getSkills().isEmpty())
            return new byte[0];
        String skillsText = freelancer.getSkills().stream()
                .map(SkillModel::getName)
                .collect(Collectors.joining(", "));
        return generateEmbedding(skillsText); // Gọi chung phương thức generateEmbedding
    }

    private byte[] hexStringToByteArray(String hex) {
        if (hex == null || hex.isEmpty()) return new byte[0];
        return new BigInteger(hex, 16).toByteArray();
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
}
