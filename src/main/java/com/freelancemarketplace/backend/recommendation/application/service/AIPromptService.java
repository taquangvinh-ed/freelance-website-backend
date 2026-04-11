package com.freelancemarketplace.backend.recommendation.application.service;

import java.util.Map;

/**
 * Application Service: AI Prompt Building & Formatting (SRP - Single Responsibility Principle)
 * This service handles prompt construction and validation
 */
public interface AIPromptService {
    
    /**
     * Build project suggestion prompt
     * @param brief Project brief
     * @param categoryName Category name
     * @param scope Project scope
     * @param timeline Project timeline
     * @param preferredSkills List of preferred skills
     * @param marketContext Market context data
     * @param locale Locale for prompt
     * @return Formatted prompt for LLM
     */
    String buildProjectSuggestionPrompt(
            String brief,
            String categoryName,
            String scope,
            String timeline,
            java.util.List<String> preferredSkills,
            String marketContext,
            String locale
    );
    
    /**
     * Build project improvement prompt
     * @param currentProject Current project data
     * @param feedback User feedback
     * @param locale Locale for prompt
     * @return Formatted prompt for LLM
     */
    String buildImprovementPrompt(
            Map<String, Object> currentProject,
            String feedback,
            String locale
    );
    
    /**
     * Validate AI response against schema
     * @param response LLM response to validate
     * @param schema Expected schema
     * @return true if valid, false otherwise
     */
    boolean validateResponse(String response, String schema);
}

