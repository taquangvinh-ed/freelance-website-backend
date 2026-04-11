package com.freelancemarketplace.backend.recommendation.application.service;

import java.util.Map;

/**
 * Application Service: AI Validation & Guardrails (SRP - Single Responsibility Principle)
 * This service handles validation and safety checks for AI-generated content
 */
public interface AIValidationService {
    
    /**
     * Validate AI-generated project data
     * @param projectData Generated project data
     * @return Map with validation results (isValid, warnings, errors)
     */
    Map<String, Object> validateProjectData(Map<String, Object> projectData);
    
    /**
     * Validate budget suggestions
     * @param budget Budget data to validate
     * @param categoryId Category for price bounds check
     * @return Corrected/validated budget data
     */
    Map<String, Object> validateBudget(Map<String, Object> budget, Long categoryId);
    
    /**
     * Check for harmful/suspicious content
     * @param text Text to check
     * @return true if content is safe, false if suspicious
     */
    boolean isSafeContent(String text);
    
    /**
     * Get guardrail warnings for suggestions
     * @param suggestions Suggestions from AI
     * @return List of warning messages
     */
    java.util.List<String> getGuardrailWarnings(Map<String, Object> suggestions);
}

