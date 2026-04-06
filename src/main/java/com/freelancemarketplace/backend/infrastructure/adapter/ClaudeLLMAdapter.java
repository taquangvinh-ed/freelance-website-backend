package com.freelancemarketplace.backend.infrastructure.adapter;

import com.freelancemarketplace.backend.application.port.LLMPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Adapter: Claude LLM Implementation (Strategy Pattern - OCP)
 * Implements LLMPort for Claude API
 * Can be swapped with OpenAILLMAdapter or GeminiLLMAdapter without changing business logic
 */
@Component("claudeLLMAdapter")
@Slf4j
public class ClaudeLLMAdapter implements LLMPort {
    
    @Value("${anthropic.api.key:${ANTHROPIC_API_KEY:${CLAUDE_API_KEY:}}}")
    private String apiKey;
    
    @Value("${anthropic.model:claude-3-sonnet-20240229}")
    private String model;
    
    private final ObjectMapper objectMapper;
    
    public ClaudeLLMAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @Override
    public String callLLM(String prompt, String model) {
        // Implementation using Claude API
        // This is a placeholder - see existing LLMServiceImp for actual implementation
        log.info("Calling Claude LLM with model: {}", model);
        
        if (!isAvailable()) {
            throw new IllegalStateException("Claude LLM is not available");
        }
        
        try {
            // Call Claude API (implementation details from existing service)
            return callClaudeAPI(prompt, model != null ? model : this.model);
        } catch (Exception e) {
            log.error("Error calling Claude API", e);
            throw new RuntimeException("Failed to call Claude LLM", e);
        }
    }
    
    @Override
    public Map<String, Object> callLLMStructured(String prompt, String model, String schema) {
        // Implementation for structured response with JSON schema
        log.info("Calling Claude LLM for structured response with model: {}", model);
        
        String response = callLLM(prompt, model);
        
        try {
            return objectMapper.readValue(response, Map.class);
        } catch (Exception e) {
            log.error("Error parsing structured response", e);
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }
    
    @Override
    public String getCurrentModel() {
        return model;
    }
    
    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }
    
    /**
     * Internal method to call Claude API
     * (Implementation details from existing LLMServiceImp)
     */
    private String callClaudeAPI(String prompt, String model) {
        // TODO: Implement actual Claude API call
        // Use OkHttp or HttpClient to call Claude API
        return null;
    }
}
