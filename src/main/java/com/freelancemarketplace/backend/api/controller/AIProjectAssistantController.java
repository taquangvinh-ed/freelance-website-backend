package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.AIProjectAssistantFrontendResponse;
import com.freelancemarketplace.backend.dto.ProjectAssistantRequest;
import com.freelancemarketplace.backend.dto.ProjectAssistantResponse;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.model.AIProjectRecommendationModel;
import com.freelancemarketplace.backend.service.AIProjectAssistantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI Project Assistant Controller
 * Provides REST endpoints for AI-powered project recommendations
 */
@RestController
@RequestMapping(path = "/api/ai/project-assistant", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AIProjectAssistantController {

    private final AIProjectAssistantService aiProjectAssistantService;

    /**
     * Main endpoint: Generate project recommendations
     * POST /api/ai/project-assistant/suggest
     * Returns: AIProjectAssistantFrontendResponse (clean schema, no ResponseDTO wrapper)
     */
    @PostMapping("/suggest")
    public ResponseEntity<?> suggestProjectContent(
            @AuthenticationPrincipal AppUser appUser,
            @Valid @RequestBody ProjectAssistantRequest request) {

        log.info("Suggest endpoint called, appUser: {}", appUser != null ? appUser.getUsername() : "NULL");

        if (appUser == null) {
            log.warn("AppUser is null - authentication failed");
            AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("User not authenticated");
            errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            log.info("Received project assistant request from user={}", appUser.getId());

            ProjectAssistantResponse response = aiProjectAssistantService.suggestProjectContent(
                    appUser.getId(), request);

            AIProjectAssistantFrontendResponse frontendResponse = mapToFrontendResponse(request, response);
            return ResponseEntity.ok(frontendResponse);

        } catch (RuntimeException e) {
            log.error("Error generating suggestions", e);
            AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            log.error("Unexpected error in suggestProjectContent", e);
            AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Internal server error");
            errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Improve existing project draft
     * POST /api/ai/project-assistant/improve/{recommendationId}
     * Returns: AIProjectAssistantFrontendResponse (same as suggest, Cách B)
     */
    @PostMapping("/improve/{recommendationId}")
    public ResponseEntity<?> improveProjectDraft(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable Long recommendationId,
            @RequestBody Map<String, String> feedbackRequest) {

        if (appUser == null) {
            AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("User not authenticated");
            errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            String feedback = feedbackRequest.get("feedback");
            if (feedback == null || feedback.isEmpty()) {
                AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Feedback is required");
                errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            ProjectAssistantResponse response = aiProjectAssistantService.improveProjectDraft(
                    appUser.getId(), recommendationId, feedback);

            // Create dummy request for mapper (improve endpoint doesn't have full request context)
            ProjectAssistantRequest dummyRequest = new ProjectAssistantRequest();
            dummyRequest.setCategoryId(1L);
            dummyRequest.setScope("MEDIUM");
            dummyRequest.setTimeline("1 to 3 months");
            dummyRequest.setExperienceLevel("INTERMEDIATE");

            AIProjectAssistantFrontendResponse frontendResponse = mapToFrontendResponse(dummyRequest, response);
            return ResponseEntity.ok(frontendResponse);

        } catch (RuntimeException e) {
            log.error("Error improving draft", e);
            AIProjectAssistantFrontendResponse errorResponse = new AIProjectAssistantFrontendResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            errorResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Record user feedback on AI recommendations
     * POST /api/ai/project-assistant/feedback/{recommendationId}
     */
    @PostMapping("/feedback/{recommendationId}")
    public ResponseEntity<?> recordFeedback(
            @AuthenticationPrincipal AppUser appUser,
            @PathVariable Long recommendationId,
            @RequestBody Map<String, String> feedbackData) {

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("401", "User not authenticated"));
        }

        try {
            String feedback = feedbackData.get("feedback"); // ACCEPTED, REJECTED, PARTIAL
            String notes = feedbackData.get("notes");

            if (feedback == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO("400", "Feedback type is required"));
            }

            AIProjectRecommendationModel.RecommendationFeedback feedbackEnum =
                    AIProjectRecommendationModel.RecommendationFeedback.valueOf(feedback.toUpperCase());

            aiProjectAssistantService.recordUserFeedback(recommendationId, feedbackEnum, notes);

            return ResponseEntity.ok(new ResponseDTO(
                    "200",
                    "Feedback recorded successfully"
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO("400", "Invalid feedback type"));
        } catch (Exception e) {
            log.error("Error recording feedback", e);
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO("400", e.getMessage()));
        }
    }

    /**
     * Get recommendation history
     * GET /api/ai/project-assistant/history?page=0&size=10
     */
    @GetMapping("/history")
    public ResponseEntity<?> getRecommendationHistory(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("401", "User not authenticated"));
        }

        try {
            var history = aiProjectAssistantService.getUserRecommendationHistory(
                    appUser.getId(), page, size);

            return ResponseEntity.ok(new ResponseDTO(
                    "200",
                    "Recommendation history retrieved",
                    history
            ));

        } catch (Exception e) {
            log.error("Error retrieving history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "Error retrieving history"));
        }
    }

    /**
     * Get user's API usage statistics
     * GET /api/ai/project-assistant/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUsageStats(
            @AuthenticationPrincipal AppUser appUser) {

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("401", "User not authenticated"));
        }

        try {
            Map<String, Object> stats = aiProjectAssistantService.getUserUsageStats(appUser.getId());

            return ResponseEntity.ok(new ResponseDTO(
                    "200",
                    "Usage statistics retrieved",
                    stats
            ));

        } catch (Exception e) {
            log.error("Error retrieving stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "Error retrieving statistics"));
        }
    }

    /**
     * Health check endpoint
     * GET /api/ai/project-assistant/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "AI Project Assistant");
        return ResponseEntity.ok(health);
    }

    // ==================== MAPPER METHODS ====================

    /**
     * Map ProjectAssistantResponse to AIProjectAssistantFrontendResponse
     * (Cách B: clean schema without ResponseDTO wrapper)
     */
    private AIProjectAssistantFrontendResponse mapToFrontendResponse(
            ProjectAssistantRequest request,
            ProjectAssistantResponse response) {

        // Extract skill names from response
        List<String> skillNames = response.getSuggestedSkills() == null
                ? Collections.emptyList()
                : response.getSuggestedSkills().stream()
                .map(ProjectAssistantResponse.SkillSuggestion::getSkillName)
                .collect(Collectors.toList());

        // Build projectDraft
        ProjectAssistantResponse.BudgetSuggestion sourceBudget = response.getBudgetSuggestion();
        AIProjectAssistantFrontendResponse.ProjectDraft projectDraft =
                new AIProjectAssistantFrontendResponse.ProjectDraft(
                        response.getSuggestedTitle(),
                        response.getSuggestedDescription(),
                        request.getCategoryId(),
                        request.getScope(),
                        skillNames,
                        toTimelineDays(request.getTimeline())
                );

        // Build budgetSuggestion
        AIProjectAssistantFrontendResponse.BudgetSuggestion budgetSuggestion =
                new AIProjectAssistantFrontendResponse.BudgetSuggestion(
                        "USD",
                        sourceBudget != null ? sourceBudget.getMinBudget() : null,
                        sourceBudget != null ? sourceBudget.getRecommendedBudget() : null,
                        sourceBudget != null ? sourceBudget.getMaxBudget() : null,
                        sourceBudget != null ? sourceBudget.getConfidence() : 0.0,
                        "market_stats_v1",
                        sourceBudget != null && sourceBudget.getMarketContext() != null
                                ? sourceBudget.getMarketContext()
                                : "Dựa trên dữ liệu thị trường hiện có.",
                        buildFactors(request)
                );

        // Build advancedPreferencesSuggestion
        AIProjectAssistantFrontendResponse.AdvancedPreferencesSuggestion advancedPrefs =
                new AIProjectAssistantFrontendResponse.AdvancedPreferencesSuggestion(
                        request.getExperienceLevel() != null ? request.getExperienceLevel() : "INTERMEDIATE",
                        toTimelineDays(request.getTimeline()),
                        20,  // hoursPerWeek default
                        "INDIVIDUAL",
                        "ANY",
                        "2-4h",
                        "CONVERSATIONAL",
                        "ONE_TIME",
                        request.getComplexityHint() != null ? request.getComplexityHint() : "MEDIUM",
                        isUrgent(request.getTimeline())
                );

        // Build body
        AIProjectAssistantFrontendResponse.Body body = new AIProjectAssistantFrontendResponse.Body(
                "ai_req_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20),
                projectDraft,
                budgetSuggestion,
                advancedPrefs,
                response.getClarifyingQuestions() != null ? response.getClarifyingQuestions() : new ArrayList<>(),
                new ArrayList<>()  // warnings - empty for now
        );

        // Build final response
        AIProjectAssistantFrontendResponse frontendResponse = new AIProjectAssistantFrontendResponse();
        frontendResponse.setSuccess(true);
        frontendResponse.setMessage("AI suggestion generated successfully");
        frontendResponse.setTimestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        frontendResponse.setBody(body);

        return frontendResponse;
    }

    /**
     * Convert timeline string to days
     */
    private int toTimelineDays(String timeline) {
        if (timeline == null) {
            return 30;
        }
        return switch (timeline) {
            case "Less than 1 month" -> 21;
            case "1 to 3 months" -> 60;
            case "3 to 6 months" -> 120;
            case "More than 6 months" -> 210;
            default -> 30;
        };
    }

    /**
     * Check if project is urgent based on timeline
     */
    private boolean isUrgent(String timeline) {
        return "Less than 1 month".equalsIgnoreCase(timeline);
    }

    /**
     * Build complexity factors for budget
     */
    private Map<String, Double> buildFactors(ProjectAssistantRequest request) {
        Map<String, Double> factors = new HashMap<>();
        factors.put("scopeWeight", 1.0);
        factors.put("complexityWeight", getComplexityWeight(request.getComplexityHint()));
        factors.put("experienceWeight", 1.0);
        factors.put("urgencyWeight", isUrgent(request.getTimeline()) ? 1.1 : 1.0);
        factors.put("locationWeight", 1.0);
        return factors;
    }

    /**
     * Get complexity weight multiplier
     */
    private double getComplexityWeight(String complexity) {
        if (complexity == null) {
            return 1.0;
        }
        return switch (complexity.toUpperCase()) {
            case "LOW" -> 0.9;
            case "HIGH" -> 1.1;
            default -> 1.0;
        };
    }
}
