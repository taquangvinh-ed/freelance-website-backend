package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProjectAssistantRequest;
import com.freelancemarketplace.backend.dto.ProjectAssistantResponse;
import com.freelancemarketplace.backend.model.AIProjectRecommendationModel;

/**
 * AI Project Assistant Service
 * Orchestrates the AI-powered project setup assistant
 * Combines pricing engine + LLM to provide comprehensive project recommendations
 */
public interface AIProjectAssistantService {

    /**
     * Main entry point: Generate comprehensive project recommendations
     * @param userId ID of the client making the request
     * @param request Project brief and preferences
     * @return AI-generated suggestions for project setup
     */
    ProjectAssistantResponse suggestProjectContent(Long userId, ProjectAssistantRequest request);

    /**
     * Improve existing project draft based on user feedback
     * @param userId Client ID
     * @param recommendationId ID of previous recommendation to improve
     * @param feedback User's feedback on previous suggestions
     * @return Improved suggestions
     */
    ProjectAssistantResponse improveProjectDraft(Long userId, Long recommendationId, String feedback);

    /**
     * Record user feedback on AI recommendations
     * Used for fine-tuning and learning
     * @param recommendationId ID of the recommendation
     * @param feedback ACCEPTED, REJECTED, PARTIAL
     * @param notes Optional notes from user
     */
    void recordUserFeedback(Long recommendationId, AIProjectRecommendationModel.RecommendationFeedback feedback, String notes);

    /**
     * Get recommendation history for a user
     * @param userId Client ID
     * @param page Page number for pagination
     * @param size Page size
     * @return List of past recommendations
     */
    java.util.List<AIProjectRecommendationModel> getUserRecommendationHistory(Long userId, int page, int size);

    /**
     * Check if user has reached rate limit
     * @param userId Client ID
     * @return true if user can make another request, false if rate limited
     */
    boolean canMakeRequest(Long userId);

    /**
     * Get user's API usage statistics
     * @param userId Client ID
     * @return Usage stats (calls, costs, etc)
     */
    java.util.Map<String, Object> getUserUsageStats(Long userId);
}

