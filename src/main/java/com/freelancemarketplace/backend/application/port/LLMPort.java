package com.freelancemarketplace.backend.application.port;

import java.util.Map;

/**
 * Port: LLM Provider Interface (OCP - Open/Closed Principle)
 * Allows implementation of different LLM providers (Claude, OpenAI, Gemini, etc.)
 */
public interface LLMPort {
    
    /**
     * Call LLM with prompt and get response
     * @param prompt The prompt to send
     * @param model Model to use
     * @return LLM response text
     */
    String callLLM(String prompt, String model);
    
    /**
     * Call LLM and get structured JSON response
     * @param prompt The prompt to send
     * @param model Model to use
     * @param schema JSON schema for response validation (optional)
     * @return Parsed JSON response as map
     */
    Map<String, Object> callLLMStructured(String prompt, String model, String schema);
    
    /**
     * Get current model name
     * @return Model identifier
     */
    String getCurrentModel();
    
    /**
     * Check if LLM provider is available
     * @return true if provider is available and authenticated
     */
    boolean isAvailable();
}

