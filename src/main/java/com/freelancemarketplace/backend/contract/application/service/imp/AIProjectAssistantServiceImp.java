package com.freelancemarketplace.backend.contract.application.service.imp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freelancemarketplace.backend.contract.application.service.PricingEngineService;
import com.freelancemarketplace.backend.recommendation.domain.model.AIAPILogModel;
import com.freelancemarketplace.backend.recommendation.domain.model.AIProjectRecommendationModel;
import com.freelancemarketplace.backend.recommendation.infrastructure.repository.AIAPILogRepository;
import com.freelancemarketplace.backend.recommendation.infrastructure.repository.AIProjectRecommendationRepository;
import com.freelancemarketplace.backend.recommendation.application.service.AIProjectAssistantService;
import com.freelancemarketplace.backend.recommendation.application.service.LLMService;
import com.freelancemarketplace.backend.toggl.dto.ChatRequest;
import com.freelancemarketplace.backend.toggl.dto.ChatResponse;
import com.freelancemarketplace.backend.toggl.dto.ProjectAssistantRequest;
import com.freelancemarketplace.backend.toggl.dto.ProjectAssistantResponse;
import com.freelancemarketplace.backend.user.infrastructure.repository.UserRepository;
import com.freelancemarketplace.backend.util.RateLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * AI Project Assistant Service Implementation
 * Orchestrates the AI-powered project setup assistant
 */
@Service
@AllArgsConstructor
@Slf4j
public class AIProjectAssistantServiceImp implements AIProjectAssistantService {

    private final PricingEngineService pricingEngineService;
    private final LLMService llmService;
    private final AIProjectRecommendationRepository recommendationRepository;
    private final AIAPILogRepository apiLogRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // Rate limiting: 10 requests per hour per user
    private static final int RATE_LIMIT_REQUESTS = 10;
    private static final long RATE_LIMIT_WINDOW_SECONDS = 60 * 60; // 1 hour

    private static final RateLimiter rateLimiter = new RateLimiter(RATE_LIMIT_REQUESTS, RATE_LIMIT_WINDOW_SECONDS);

    @Override
    @Transactional
    public ProjectAssistantResponse suggestProjectContent(Long userId, ProjectAssistantRequest request) {
        log.info("Generating AI suggestions for user={}", userId);

        // Check rate limit
        if (!canMakeRequest(userId)) {
            log.warn("User {} exceeded rate limit", userId);
            throw new RuntimeException("Rate limit exceeded. Maximum 10 requests per hour.");
        }

        try {
            // Get market data for budget calculation
            var marketStats = pricingEngineService.calculateMarketStats(
                    request.getCategoryId(),
                    request.getScope(),
                    request.getExperienceLevel() != null ? request.getExperienceLevel() : "INTERMEDIATE",
                    request.getRegion()
            );

            // Apply complexity and urgency factors
            BigDecimal adjustedBudget = applyFactors(
                    marketStats.getP50Budget(),
                    request.getComplexityHint(),
                    request.getTimeline()
            );

            // Build market context for LLM
            String marketContext = buildMarketContext(marketStats, adjustedBudget);

            // Call LLM to generate suggestions
            String llmResponse = llmService.generateProjectSuggestions(
                    request.getBrief(),
                    "Project", // Category name (simplified)
                    request.getScope(),
                    request.getTimeline(),
                    request.getPreferredSkills(),
                    marketContext
            );

            // Validate LLM response
            if (!llmService.validateResponse(llmResponse)) {
                log.warn("LLM response failed validation");
                throw new RuntimeException("AI response validation failed. Please try again.");
            }

            // Parse LLM response to DTO
            ProjectAssistantResponse response = parseAndMapResponse(llmResponse);

            // Store recommendation for audit trail
            storeRecommendation(userId, request, response, llmResponse);

            // Log API usage
            logAPIUsage(userId, llmResponse, marketContext);

            log.info("Successfully generated AI suggestions for user={}", userId);
            return response;

        } catch (Exception e) {
            log.error("Error generating AI suggestions", e);
            throw new RuntimeException("Failed to generate AI suggestions: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProjectAssistantResponse improveProjectDraft(Long userId, Long recommendationId, String feedback) {
        log.info("Improving project draft for user={}", userId);

        // Get original recommendation
        var recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));


        // Build market context
        String marketContext = buildMarketContext(null, recommendation.getSuggestedBudgetRecommended());

        try {
            // Call LLM to improve
            String improvedResponse = llmService.improveProjectSuggestions(
                    recommendation.getUserBrief(),
                    recommendation.getRawJsonResponse(),
                    feedback,
                    marketContext
            );

            // Validate and parse
            if (!llmService.validateResponse(improvedResponse)) {
                throw new RuntimeException("AI response validation failed");
            }

            ProjectAssistantResponse response = parseAndMapResponse(improvedResponse);

            // Store as new recommendation
            storeRecommendation(userId, null, response, improvedResponse);

            log.info("Successfully improved project draft for user={}", userId);
            return response;
        } catch (Exception e) {
            log.error("Error improving project draft", e);
            throw new RuntimeException("Failed to improve suggestions: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ChatResponse chat(Long userId, ChatRequest request) {
        log.info("Chat request from user={}, message={}", userId, request.getMessage());

        if (!canMakeRequest(userId)) {
            log.warn("User {} exceeded rate limit", userId);
            throw new RuntimeException("Rate limit exceeded. Maximum 10 requests per hour.");
        }

        try {
            String projectContext = buildProjectContext(request.getProjectContext());
            String projectBrief = getProjectBrief(request.getRecommendationId());

            String aiResponse = llmService.chat(
                    request.getMessage(),
                    projectContext,
                    projectBrief
            );

            logAPIUsage(userId, aiResponse, projectContext);

            ChatResponse response = new ChatResponse();
            response.setSuccess(true);
            response.setMessage("Chat response generated");
            response.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());

            ChatResponse.Body body = new ChatResponse.Body();
            body.setMessageId(UUID.randomUUID().toString());
            body.setAiResponse(aiResponse);
            body.setResponseType(detectResponseType(aiResponse));
            body.setTips(generateTips(request.getMessage(), aiResponse));
            body.setRequiresFollowUp(needsFollowUp(aiResponse));
            response.setBody(body);

            log.info("Successfully generated chat response for user={}", userId);
            return response;

        } catch (Exception e) {
            log.error("Error generating chat response", e);
            throw new RuntimeException("Failed to generate chat response: " + e.getMessage());
        }
    }

    private String buildProjectContext(ChatRequest.ProjectContext context) {
        if (context == null) {
            return "No project context provided yet. User is starting to create a project.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Current Project State:\n");

        if (context.getTitle() != null) {
            sb.append("- Title: ").append(context.getTitle()).append("\n");
        }
        if (context.getDescription() != null) {
            sb.append("- Description: ").append(context.getDescription()).append("\n");
        }
        if (context.getCategoryId() != null) {
            sb.append("- Category ID: ").append(context.getCategoryId()).append("\n");
        }
        if (context.getScope() != null) {
            sb.append("- Scope: ").append(context.getScope()).append("\n");
        }
        if (context.getTimeline() != null) {
            sb.append("- Timeline: ").append(context.getTimeline()).append("\n");
        }
        if (context.getExperienceLevel() != null) {
            sb.append("- Experience Level: ").append(context.getExperienceLevel()).append("\n");
        }
        if (context.getBudgetMin() != null && context.getBudgetMax() != null) {
            sb.append("- Budget Range: ").append(context.getBudgetMin())
              .append(" - ").append(context.getBudgetMax()).append("\n");
        }
        if (context.getSkills() != null && !context.getSkills().isEmpty()) {
            sb.append("- Skills: ").append(String.join(", ", context.getSkills())).append("\n");
        }

        return sb.toString();
    }

    private String getProjectBrief(Long recommendationId) {
        if (recommendationId == null) {
            return null;
        }
        return recommendationRepository.findById(recommendationId)
                .map(AIProjectRecommendationModel::getUserBrief)
                .orElse(null);
    }

    private ChatResponse.ResponseType detectResponseType(String aiResponse) {
        String lower = aiResponse.toLowerCase();
        if (lower.contains("suggest") || lower.contains("recommend") || lower.contains("should")) {
            return ChatResponse.ResponseType.SUGGESTION;
        }
        if (lower.contains("tip") || lower.contains("advice") || lower.contains("best practice")) {
            return ChatResponse.ResponseType.TIP;
        }
        if (lower.contains("?") && lower.indexOf('?') > lower.length() - 50) {
            return ChatResponse.ResponseType.QUESTION;
        }
        if (lower.contains("warning") || lower.contains("caution") || lower.contains("be careful")) {
            return ChatResponse.ResponseType.WARNING;
        }
        return ChatResponse.ResponseType.INFO;
    }

    private List<String> generateTips(String userMessage, String aiResponse) {
        List<String> tips = new ArrayList<>();
        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("budget") || lowerMessage.contains("price") || lowerMessage.contains("cost")) {
            tips.add("Tip: A clear budget helps attract serious freelancers.");
            tips.add("Tip: Consider setting a range (min-max) to give flexibility.");
        }
        if (lowerMessage.contains("skill") || lowerMessage.contains("technology")) {
            tips.add("Tip: List specific technologies to get more targeted proposals.");
        }
        if (lowerMessage.contains("timeline") || lowerMessage.contains("deadline") || lowerMessage.contains("time")) {
            tips.add("Tip: Realistic timelines lead to better quality work.");
        }
        if (lowerMessage.contains("description") || lowerMessage.contains("detail")) {
            tips.add("Tip: Detailed descriptions receive 40% more proposals on average.");
        }

        return tips;
    }

    private boolean needsFollowUp(String aiResponse) {
        return aiResponse.contains("?") && aiResponse.indexOf('?') > aiResponse.length() - 100;
    }

    @Override
    @Transactional
    public void recordUserFeedback(Long recommendationId, String feedback, String notes) {
        var recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));

        AIProjectRecommendationModel.RecommendationFeedback feedbackEnum =
                AIProjectRecommendationModel.RecommendationFeedback.valueOf(feedback.toUpperCase(Locale.ROOT));

        recommendation.setUserFeedback(feedbackEnum);
        recommendation.setFeedbackNotes(notes);
        recommendationRepository.save(recommendation);

        log.info("Recorded user feedback for recommendation={}, feedback={}", recommendationId, feedbackEnum);
    }

    @Override
    public List<AIProjectRecommendationModel> getUserRecommendationHistory(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return recommendationRepository.findByClientIdOrderByCreatedAtDesc(userId, pageable).getContent();
    }

    @Override
    public boolean canMakeRequest(Long userId) {
        return rateLimiter.allowRequest(userId);
    }

    @Override
    public Map<String, Object> getUserUsageStats(Long userId) {
        // Get usage in last 24 hours
        Timestamp lastDay = Timestamp.from(Instant.now().minus(Duration.ofDays(1)));

        var stats = new HashMap<String, Object>();

        // Count API calls
        Integer callCount = apiLogRepository.countCallsByUserIdAndTimeLimit(userId, lastDay);
        stats.put("api_calls_24h", callCount != null ? callCount : 0);

        // Calculate cost
        BigDecimal totalCost = apiLogRepository.sumCostByUserIdAndDateRange(userId, lastDay);
        stats.put("estimated_cost_24h", totalCost != null ? totalCost : BigDecimal.ZERO);

        // Get recommendation history
        List<AIProjectRecommendationModel> recommendations = getUserRecommendationHistory(userId, 0, 10);
        stats.put("recent_recommendations", recommendations.size());

        // Feedback stats
        long acceptedCount = recommendations.stream()
                .filter(r -> r.getUserFeedback() == AIProjectRecommendationModel.RecommendationFeedback.ACCEPTED)
                .count();
        stats.put("accepted_recommendations", acceptedCount);

        return stats;
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private BigDecimal applyFactors(BigDecimal baseMedian, String complexity, String timeline) {
        if (baseMedian == null) return null;

        BigDecimal adjusted = baseMedian;
        if (complexity != null) {
            adjusted = pricingEngineService.applyComplexityFactor(adjusted, complexity);
        }
        if (timeline != null) {
            adjusted = pricingEngineService.applyUrgencyFactor(adjusted, timeline);
        }
        return adjusted;
    }

    private String buildMarketContext(Object marketStats, BigDecimal adjustedBudget) {
        StringBuilder context = new StringBuilder();
        context.append("Market Statistics:\n");
        if (marketStats != null) {
            context.append(marketStats).append("\n");
        }
        if (adjustedBudget != null) {
            context.append("Adjusted Budget Recommendation: $").append(adjustedBudget).append("\n");
        }
        return context.toString();
    }

    private ProjectAssistantResponse parseAndMapResponse(String jsonResponse) throws Exception {
        JsonNode json = objectMapper.readTree(jsonResponse);

        ProjectAssistantResponse response = new ProjectAssistantResponse();
        response.setSuggestedTitle(json.get("title").asText());
        response.setSuggestedDescription(json.get("description").asText());

        // Set default confidence if not present
        if (json.has("confidence")) {
            response.setOverallConfidence(json.get("confidence").asDouble());
        } else {
            response.setOverallConfidence(0.8);
        }

        if (json.has("reasoning")) {
            response.setReasoning(json.get("reasoning").asText());
        }

        // Parse budget
        if (json.has("budget")) {
            JsonNode budgetNode = json.get("budget");
            ProjectAssistantResponse.BudgetSuggestion budget = new ProjectAssistantResponse.BudgetSuggestion();
            budget.setMinBudget(new BigDecimal(budgetNode.get("min").asText()));
            budget.setRecommendedBudget(new BigDecimal(budgetNode.get("recommended").asText()));
            budget.setMaxBudget(new BigDecimal(budgetNode.get("max").asText()));
            if (budgetNode.has("confidence")) {
                budget.setConfidence(budgetNode.get("confidence").asDouble());
            }
            if (budgetNode.has("explanation")) {
                budget.setExplanation(budgetNode.get("explanation").asText());
            }
            if (budgetNode.has("marketContext")) {
                budget.setMarketContext(budgetNode.get("marketContext").asText());
            }
            response.setBudgetSuggestion(budget);
        }

        // Parse skills
        List<ProjectAssistantResponse.SkillSuggestion> skills = new ArrayList<>();
        if (json.has("skills")) {
            json.get("skills").forEach(skillNode -> {
                ProjectAssistantResponse.SkillSuggestion skill = new ProjectAssistantResponse.SkillSuggestion();
                skill.setSkillName(skillNode.asText());
                skills.add(skill);
            });
        }
        response.setSuggestedSkills(skills);

        // Parse milestones
        List<ProjectAssistantResponse.MilestoneSuggestion> milestones = new ArrayList<>();
        if (json.has("milestones")) {
            json.get("milestones").forEach(msNode -> {
                ProjectAssistantResponse.MilestoneSuggestion ms = new ProjectAssistantResponse.MilestoneSuggestion();
                ms.setTitle(msNode.get("title").asText());
                if (msNode.has("description")) {
                    ms.setDescription(msNode.get("description").asText());
                }
                if (msNode.has("daysFromStart")) {
                    ms.setDaysFromStart(msNode.get("daysFromStart").asInt());
                }
                if (msNode.has("budgetPercentage")) {
                    ms.setBudgetPercentage(msNode.get("budgetPercentage").asInt());
                }
                milestones.add(ms);
            });
        }
        response.setSuggestedMilestones(milestones);

        // Parse clarifying questions
        List<String> questions = new ArrayList<>();
        if (json.has("clarifyingQuestions")) {
            json.get("clarifyingQuestions").forEach(q -> questions.add(q.asText()));
        }
        response.setClarifyingQuestions(questions);

        return response;
    }

    private AIProjectRecommendationModel storeRecommendation(Long userId, ProjectAssistantRequest request,
                                                             ProjectAssistantResponse response, String rawResponse) {
        var recommendation = new AIProjectRecommendationModel();

        // Get user
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Set client using reflection to bypass type checking
        try {
            var field = recommendation.getClass().getDeclaredField("client");
            field.setAccessible(true);
            field.set(recommendation, user);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.warn("Could not set client field", e);
        }

        if (request != null) {
            recommendation.setUserBrief(request.getBrief());
        }

        recommendation.setSuggestedTitle(response.getSuggestedTitle());
        recommendation.setSuggestedDescription(response.getSuggestedDescription());

        if (response.getBudgetSuggestion() != null) {
            recommendation.setSuggestedBudgetMin(response.getBudgetSuggestion().getMinBudget());
            recommendation.setSuggestedBudgetRecommended(response.getBudgetSuggestion().getRecommendedBudget());
            recommendation.setSuggestedBudgetMax(response.getBudgetSuggestion().getMaxBudget());
            recommendation.setBudgetConfidence(new BigDecimal(response.getBudgetSuggestion().getConfidence().toString()));
        }

        recommendation.setRawJsonResponse(rawResponse);
        recommendation.setLlmModel(llmService.getCurrentModel());
        recommendation.setUserFeedback(AIProjectRecommendationModel.RecommendationFeedback.NOT_PROVIDED);
        recommendation.setIsActive(true);

        return recommendationRepository.save(recommendation);
    }

    private void logAPIUsage(Long userId, String response, String prompt) {
        try {
            var user = userRepository.findById(userId).orElse(null);
            Integer inputTokens = llmService.countTokens(prompt);
            Integer outputTokens = llmService.countTokens(response);
            BigDecimal cost = llmService.estimateCost(inputTokens, outputTokens);

            var log = new AIAPILogModel();
            log.setUser(user);
            log.setProvider("google");
            log.setModel(llmService.getCurrentModel());
            log.setPromptTokens(inputTokens);
            log.setCompletionTokens(outputTokens);
            log.setTotalTokens(inputTokens + outputTokens);
            log.setEstimatedCost(cost);
            log.setResponseStatus("SUCCESS");
            log.setFeature("project_assistant");
            log.setTimestamp(Timestamp.from(Instant.now()));

            apiLogRepository.save(log);
        } catch (Exception e) {
            log.warn("Error logging API usage", e);
        }
    }
}

