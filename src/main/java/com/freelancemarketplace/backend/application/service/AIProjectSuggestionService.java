package com.freelancemarketplace.backend.application.service;

import java.util.Map;

/**
 * Application Service: AI Project Suggestion (SRP - Single Responsibility Principle)
 * This service handles ONLY AI-powered project suggestions
 */
public interface AIProjectSuggestionService {
    
    /**
     * Generate AI project suggestions based on brief
     * @param brief Project brief from user
     * @param locale Locale for response (vi-VN, en-US, etc.)
     * @param currency Currency for budget suggestions (USD, VND, etc.)
     * @param clientContext Additional context about client
     * @return Structured suggestion as map
     */
    Map<String, Object> generateSuggestions(
            String brief,
            String locale,
            String currency,
            Map<String, Object> clientContext
    );
    
    /**
     * Improve existing project draft based on feedback
     * @param projectId Project ID to improve
     * @param feedback Feedback from user
     * @param locale Locale for response
     * @return Improved suggestions as map
     */
    Map<String, Object> improveSuggestions(
            Long projectId,
            String feedback,
            String locale
    );
}

