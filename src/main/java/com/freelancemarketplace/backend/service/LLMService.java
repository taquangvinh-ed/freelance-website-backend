package com.freelancemarketplace.backend.service;

import java.math.BigDecimal;

/**
 * LLM Integration Service
 * Handles all communication with Language Model APIs
 */
public interface LLMService {

    /**
     * Generate AI recommendations for a project
     * @param brief User's project brief
     * @param categoryName Category of the project
     * @param scope SMALL, MEDIUM, LARGE, ENTERPRISE
     * @param timeline Project timeline
     * @param preferredSkills Optional: skills user already knows they need
     * @param marketContext Market data context (JSON string with budget stats)
     * @return Raw JSON response from LLM
     */
    String generateProjectSuggestions(
            String brief,
            String categoryName,
            String scope,
            String timeline,
            java.util.List<String> preferredSkills,
            String marketContext
    );

    /**
     * Call LLM to improve existing suggestions
     * @param originalBrief Original project brief
     * @param previousSuggestions Previous AI suggestions
     * @param userFeedback User feedback on previous suggestions
     * @param marketContext Market data context
     * @return Improved suggestions from LLM
     */
    String improveProjectSuggestions(
            String originalBrief,
            String previousSuggestions,
            String userFeedback,
            String marketContext
    );

    /**
     * Count tokens in a prompt (for cost estimation)
     * @param text Text to count tokens for
     * @return Number of tokens
     */
    Integer countTokens(String text);

    /**
     * Get estimated cost of LLM call
     * @param inputTokens Number of input tokens
     * @param outputTokens Number of output tokens
     * @return Estimated cost in USD
     */
    BigDecimal estimateCost(Integer inputTokens, Integer outputTokens);

    /**
     * Validate LLM response against guardrails
     * @param response JSON response from LLM
     * @return true if response passes all guardrails, false otherwise
     */
    boolean validateResponse(String response);

    /**
     * Get current LLM model being used
     * @return Model name (e.g., claude-3-haiku)
     */
    String getCurrentModel();

    /**
     * Get pricing info for current model
     * @return Map with input_price_per_1m_tokens and output_price_per_1m_tokens
     */
    java.util.Map<String, BigDecimal> getModelPricing();
}

