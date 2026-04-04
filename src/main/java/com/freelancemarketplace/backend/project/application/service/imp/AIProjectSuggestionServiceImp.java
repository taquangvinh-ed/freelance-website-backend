package com.freelancemarketplace.backend.project.application.service.imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelancemarketplace.backend.project.dto.AiSuggestProjectResponseDTO;
import com.freelancemarketplace.backend.project.dto.AiSuggestedProjectDTO;
import com.freelancemarketplace.backend.exceptionHandling.ApiException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.category.infrastructure.repository.CategoriesRepository;
import com.freelancemarketplace.backend.project.api.request.AiSuggestProjectRequest;
import com.freelancemarketplace.backend.skill.infrastructure.repository.SkillsRepository;
import com.freelancemarketplace.backend.project.application.service.AIProjectSuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIProjectSuggestionServiceImp implements AIProjectSuggestionService {

    private static final int MAX_REQUESTS_PER_HOUR = 10;
    private static final Map<Long, Deque<Instant>> REQUEST_TIMELINE = new ConcurrentHashMap<>();

    private static final Set<String> ALLOWED_DURATIONS = Set.of(
            "Less than 1 month", "1 to 3 months", "3 to 6 months", "More than 6 months"
    );

    private static final Set<String> ALLOWED_LEVELS = Set.of(
            "Entry level", "Intermediate", "Expert"
    );

    private static final Set<String> ALLOWED_WORKLOADS = Set.of(
            "Part-time", "Full-time", "Flexible"
    );

    private static final Set<String> ALLOWED_BUDGET_TYPES = Set.of("FIXED_PRICE", "HOURLY_RATE");

    private static final Set<String> ALLOWED_HOURS_PER_WEEK = Set.of("less10", "10to20", "20to30", "30plus");

    private final ObjectMapper objectMapper;
    private final CategoriesRepository categoriesRepository;
    private final SkillsRepository skillsRepository;

    @Value("${ANTHROPIC_API_KEY:}")
    private String anthropicApiKey;

    @Value("${ai.claude.api-url:https://api.anthropic.com/v1/messages}")
    private String anthropicApiUrl;

    @Value("${ai.claude.model:claude-3-5-sonnet-latest}")
    private String anthropicModel;

    @Value("${ai.claude.timeout-seconds:20}")
    private Integer timeoutSeconds;

    @Override
    public AiSuggestProjectResponseDTO suggestProject(Long userId, AiSuggestProjectRequest request) {
        validateRateLimit(userId);

        if (anthropicApiKey == null || anthropicApiKey.isBlank()) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.CONFIGURATION_ERROR,
                    "ANTHROPIC_API_KEY is missing. Please set it in environment variables.");
        }

        String requestId = "ai_req_" + UUID.randomUUID();
        List<String> warnings = new ArrayList<>();

        try {
            String systemPrompt = buildSystemPrompt();
            String userPrompt = buildUserPrompt(request);
            String llmRaw = callClaude(systemPrompt, userPrompt, requestId);

            AiSuggestedProjectDTO suggestedProject = parseSuggestedProject(llmRaw);
            normalizeSuggestion(suggestedProject, request, warnings);
            addMappingWarnings(suggestedProject, warnings);

            return new AiSuggestProjectResponseDTO(suggestedProject, warnings, requestId);
        } catch (Exception ex) {
            log.error("AI suggest failed. requestId={}, userId={}, reason={}", requestId, userId, ex.getMessage());
            AiSuggestedProjectDTO fallback = buildFallbackSuggestion(request);
            warnings.add("AI temporarily unavailable. Returned safe fallback suggestion.");
            warnings.add("Please review all fields before submit.");
            return new AiSuggestProjectResponseDTO(fallback, warnings, requestId);
        }
    }

    private void validateRateLimit(Long userId) {
        Instant now = Instant.now();
        Instant cutoff = now.minus(Duration.ofHours(1));

        Deque<Instant> timeline = REQUEST_TIMELINE.computeIfAbsent(userId, id -> new ArrayDeque<>());

        synchronized (timeline) {
            while (!timeline.isEmpty() && timeline.peekFirst().isBefore(cutoff)) {
                timeline.pollFirst();
            }
            if (timeline.size() >= MAX_REQUESTS_PER_HOUR) {
                throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.RATE_LIMIT_EXCEEDED,
                        "Rate limit exceeded: max 10 AI requests per hour.");
            }
            timeline.addLast(now);
        }
    }

    private String callClaude(String systemPrompt, String userPrompt, String requestId) throws IOException, InterruptedException {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", anthropicModel);
        payload.put("max_tokens", 1400);
        payload.put("temperature", 0.2);
        payload.put("system", systemPrompt);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);
        payload.put("messages", List.of(userMessage));

        String requestBody = objectMapper.writeValueAsString(payload);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(anthropicApiUrl))
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .header("Content-Type", "application/json")
                .header("x-api-key", anthropicApiKey)
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = null;
        for (int attempt = 1; attempt <= 2; attempt++) {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 500) {
                break;
            }
        }

        if (response == null) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Claude API did not return a response");
        }

        if (response.statusCode() == 401) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Claude API 401. Check ANTHROPIC_API_KEY and project permissions.");
        }

        if (response.statusCode() >= 400) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Claude API call failed: HTTP " + response.statusCode());
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode contentNode = root.path("content");
        if (!contentNode.isArray() || contentNode.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Claude API returned empty content");
        }

        String text = contentNode.get(0).path("text").asText();
        if (text == null || text.isBlank()) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Claude API returned blank text content");
        }

        log.info("Claude request success. requestId={}, model={}, outputChars={}", requestId, anthropicModel, text.length());
        return cleanJsonString(text);
    }

    private String cleanJsonString(String raw) {
        String cleaned = raw.trim();
        if (cleaned.startsWith("```") && cleaned.endsWith("```")) {
            int firstNewLine = cleaned.indexOf('\n');
            cleaned = firstNewLine >= 0 ? cleaned.substring(firstNewLine + 1) : cleaned;
            cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }
        return cleaned;
    }

    private AiSuggestedProjectDTO parseSuggestedProject(String llmRaw) throws IOException {
        return objectMapper.readValue(llmRaw, AiSuggestedProjectDTO.class);
    }

    private void normalizeSuggestion(AiSuggestedProjectDTO project, AiSuggestProjectRequest request, List<String> warnings) {
        if (project.getTitle() == null || project.getTitle().isBlank()) {
            project.setTitle("Project based on your brief");
            warnings.add("Title was missing from AI output and has been defaulted.");
        }

        if (project.getDescription() == null || project.getDescription().isBlank()) {
            project.setDescription("Please refine requirements, deliverables, timeline and acceptance criteria.");
            warnings.add("Description was missing from AI output and has been defaulted.");
        }

        if (project.getCategoryName() == null || project.getCategoryName().isBlank()) {
            project.setCategoryName("Web Development");
            warnings.add("Category was missing from AI output and has been defaulted.");
        }

        if (project.getSkillNames() == null) {
            project.setSkillNames(new ArrayList<>());
        }

        List<String> cleanedSkills = project.getSkillNames().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .limit(8)
                .collect(Collectors.toList());

        if (cleanedSkills.size() < 4) {
            cleanedSkills.addAll(List.of("Communication", "Problem Solving", "Project Planning"));
            cleanedSkills = cleanedSkills.stream().distinct().limit(8).collect(Collectors.toList());
            warnings.add("Skill list was expanded to minimum 4 items.");
        }
        project.setSkillNames(cleanedSkills);

        if (project.getScope() == null) {
            project.setScope(new AiSuggestedProjectDTO.Scope());
            warnings.add("Scope was missing from AI output and has been defaulted.");
        }

        if (!ALLOWED_DURATIONS.contains(project.getScope().getDuration())) {
            project.getScope().setDuration("1 to 3 months");
        }

        if (!ALLOWED_LEVELS.contains(project.getScope().getLevel())) {
            project.getScope().setLevel("Intermediate");
        }

        if (!ALLOWED_WORKLOADS.contains(project.getScope().getWorkload())) {
            project.getScope().setWorkload("Flexible");
        }

        if (project.getBudget() == null) {
            project.setBudget(new AiSuggestedProjectDTO.Budget());
            warnings.add("Budget was missing from AI output and has been defaulted.");
        }

        normalizeBudget(project, request, warnings);

        if (project.getAdvancedPreferences() == null) {
            project.setAdvancedPreferences(new AiSuggestedProjectDTO.AdvancedPreferences());
        }

        if (!ALLOWED_HOURS_PER_WEEK.contains(project.getAdvancedPreferences().getHoursPerWeek())) {
            project.getAdvancedPreferences().setHoursPerWeek("10to20");
        }

        if (project.getAdvancedPreferences().getIsRemote() == null) {
            project.getAdvancedPreferences().setIsRemote(Boolean.TRUE);
        }

        if (project.getAdvancedPreferences().getMinYearsExperience() == null) {
            project.getAdvancedPreferences().setMinYearsExperience(1);
        }

        if (project.getAdvancedPreferences().getPreferredLanguages() == null) {
            project.getAdvancedPreferences().setPreferredLanguages(new ArrayList<>());
        }

        if (project.getConfidence() == null) {
            project.setConfidence(0.75);
        }
        project.setConfidence(Math.max(0.0, Math.min(1.0, project.getConfidence())));

        if (project.getNotes() == null) {
            project.setNotes(new ArrayList<>());
        }
        if (project.getNotes().isEmpty()) {
            project.getNotes().add("AI suggestion only. Please review and adjust before posting.");
        }
    }

    private void normalizeBudget(AiSuggestedProjectDTO project, AiSuggestProjectRequest request, List<String> warnings) {
        String budgetType = project.getBudget().getType();
        if (!ALLOWED_BUDGET_TYPES.contains(budgetType)) {
            project.getBudget().setType("FIXED_PRICE");
            budgetType = "FIXED_PRICE";
        }

        String currency = request.getCurrency();
        if (currency == null || currency.isBlank()) {
            currency = "USD";
        }
        project.getBudget().setCurrencyUnit(currency);

        if ("HOURLY_RATE".equals(budgetType)) {
            BigDecimal min = project.getBudget().getMinValue();
            BigDecimal max = project.getBudget().getMaxValue();

            if (min == null || min.compareTo(BigDecimal.ZERO) <= 0) {
                min = new BigDecimal("10");
            }
            if (max == null || max.compareTo(min) <= 0) {
                max = min.add(new BigDecimal("20"));
            }

            project.getBudget().setMinValue(min);
            project.getBudget().setMaxValue(max);
            project.getBudget().setFixedValue(null);
        } else {
            BigDecimal fixed = project.getBudget().getFixedValue();
            if (fixed == null || fixed.compareTo(BigDecimal.ZERO) <= 0) {
                fixed = new BigDecimal("1000");
                warnings.add("Fixed budget was missing/invalid and has been defaulted.");
            }

            project.getBudget().setFixedValue(fixed);
            project.getBudget().setMinValue(null);
            project.getBudget().setMaxValue(null);
        }
    }

    private void addMappingWarnings(AiSuggestedProjectDTO suggestion, List<String> warnings) {
        if (categoriesRepository.findByNameIgnoreCase(suggestion.getCategoryName()).isEmpty()) {
            warnings.add("Category '" + suggestion.getCategoryName() + "' does not match existing backend category exactly.");
        }

        List<String> notFoundSkills = suggestion.getSkillNames().stream()
                .filter(skillName -> skillsRepository.findByNameIgnoreCase(skillName).isEmpty())
                .toList();

        if (!notFoundSkills.isEmpty()) {
            warnings.add("Some skills are not in backend skill list yet: " + String.join(", ", notFoundSkills));
        }
    }

    private AiSuggestedProjectDTO buildFallbackSuggestion(AiSuggestProjectRequest request) {
        AiSuggestedProjectDTO fallback = new AiSuggestedProjectDTO();
        fallback.setTitle("New project suggestion");
        fallback.setDescription("Please describe objective, scope, deliverables, technical requirements and acceptance criteria.");
        fallback.setCategoryName("Web Development");
        fallback.setSkillNames(new ArrayList<>(List.of("Communication", "Project Planning", "Problem Solving", "Documentation")));

        AiSuggestedProjectDTO.Scope scope = new AiSuggestedProjectDTO.Scope();
        scope.setDuration("1 to 3 months");
        scope.setLevel("Intermediate");
        scope.setWorkload("Flexible");
        fallback.setScope(scope);

        AiSuggestedProjectDTO.Budget budget = new AiSuggestedProjectDTO.Budget();
        budget.setType("FIXED_PRICE");
        budget.setCurrencyUnit(request.getCurrency() == null || request.getCurrency().isBlank() ? "USD" : request.getCurrency());
        budget.setFixedValue(new BigDecimal("1000"));
        fallback.setBudget(budget);

        AiSuggestedProjectDTO.AdvancedPreferences preferences = new AiSuggestedProjectDTO.AdvancedPreferences();
        preferences.setHoursPerWeek("10to20");
        preferences.setIsRemote(Boolean.TRUE);
        preferences.setMinYearsExperience(1);
        preferences.setPreferTopRated(Boolean.FALSE);
        preferences.setPreferVerified(Boolean.FALSE);
        fallback.setAdvancedPreferences(preferences);

        fallback.setConfidence(0.5);
        fallback.setNotes(new ArrayList<>(List.of("Fallback generated due to temporary AI issue.")));

        return fallback;
    }

    private String buildSystemPrompt() {
        return "You are a senior marketplace project-scoping assistant.\n" +
                "Goal: Convert a client's free-text brief into a complete and realistic project posting draft.\n" +
                "Hard requirements:\n" +
                "1) Return strict JSON only. No markdown, no explanation.\n" +
                "2) Follow exact schema.\n" +
                "3) Conservative realistic assumptions.\n" +
                "4) If info missing, infer and add assumptions to notes.\n" +
                "5) For budget type HOURLY_RATE: minValue and maxValue required, minValue < maxValue, fixedValue omitted.\n" +
                "6) For budget type FIXED_PRICE: fixedValue required, minValue/maxValue omitted.\n" +
                "7) scope.duration must be one of: Less than 1 month, 1 to 3 months, 3 to 6 months, More than 6 months.\n" +
                "8) scope.level must be one of: Entry level, Intermediate, Expert.\n" +
                "9) scope.workload must be one of: Part-time, Full-time, Flexible.\n" +
                "10) confidence in [0,1], skillNames between 4 and 8 items.\n" +
                "Output JSON schema:\n" +
                "{\n" +
                "  \"title\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"categoryName\": \"string\",\n" +
                "  \"skillNames\": [\"string\"],\n" +
                "  \"scope\": {\n" +
                "    \"duration\": \"string\",\n" +
                "    \"level\": \"string\",\n" +
                "    \"workload\": \"string\"\n" +
                "  },\n" +
                "  \"budget\": {\n" +
                "    \"type\": \"FIXED_PRICE | HOURLY_RATE\",\n" +
                "    \"currencyUnit\": \"string\",\n" +
                "    \"minValue\": 0,\n" +
                "    \"maxValue\": 0,\n" +
                "    \"fixedValue\": 0\n" +
                "  },\n" +
                "  \"advancedPreferences\": {\n" +
                "    \"hoursPerWeek\": \"less10 | 10to20 | 20to30 | 30plus\",\n" +
                "    \"isRemote\": true,\n" +
                "    \"preferredTimezone\": \"string\",\n" +
                "    \"minYearsExperience\": 1,\n" +
                "    \"preferredLanguages\": [\"string\"],\n" +
                "    \"preferVerified\": false,\n" +
                "    \"preferTopRated\": false\n" +
                "  },\n" +
                "  \"confidence\": 0.0,\n" +
                "  \"notes\": [\"string\"]\n" +
                "}";
    }

    private String buildUserPrompt(AiSuggestProjectRequest request) {
        String locale = request.getLocale() == null || request.getLocale().isBlank() ? "vi-VN" : request.getLocale();
        String currency = request.getCurrency() == null || request.getCurrency().isBlank() ? "USD" : request.getCurrency();

        String clientContextString = "null";
        try {
            if (request.getClientContext() != null) {
                clientContextString = objectMapper.writeValueAsString(request.getClientContext());
            }
        } catch (Exception ignored) {
            clientContextString = "null";
        }

        return "Generate project suggestion from this input:\n\n" +
                "locale: " + locale + "\n" +
                "currency: " + currency + "\n" +
                "clientContext: " + clientContextString + "\n\n" +
                "clientPrompt:\n" + request.getPrompt();
    }
}

