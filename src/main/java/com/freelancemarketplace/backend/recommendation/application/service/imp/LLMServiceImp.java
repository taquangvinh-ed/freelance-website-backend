package com.freelancemarketplace.backend.recommendation.application.service.imp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freelancemarketplace.backend.recommendation.application.service.LLMService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * LLM Service Implementation using Claude API via OkHttp
 * Direct HTTP calls - no SDK dependency
 */
@Service
@Slf4j
public class LLMServiceImp implements LLMService {

    @Value("${ai.llm.api-key:}")
    private String apiKey;

    @Value("${ai.llm.model:claude-3-5-haiku-20241022}")
    private String model;

    @Value("${ai.llm.api-url:https://api.anthropic.com/v1/messages}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient httpClient;

    // Claude 3.5 Haiku pricing (per 1M tokens)
    private static final BigDecimal INPUT_PRICE_PER_1M = new BigDecimal("0.80");
    private static final BigDecimal OUTPUT_PRICE_PER_1M = new BigDecimal("4.00");

    public LLMServiceImp() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Generate project suggestions using Claude API
     */
    @Override
    public String generateProjectSuggestions(
            String brief,
            String categoryName,
            String scope,
            String timeline,
            List<String> preferredSkills,
            String marketContext) {

        String prompt = buildProjectSuggestionPrompt(brief, categoryName, scope, timeline, preferredSkills, marketContext);
        try {
            return callClaudeAPI(prompt);
        } catch (Exception e) {
            log.error("Error generating project suggestions", e);
            throw new RuntimeException("Failed to generate suggestions: " + e.getMessage(), e);
        }
    }

    /**
     * Improve existing project suggestions
     */
    @Override
    public String improveProjectSuggestions(
            String originalBrief,
            String previousSuggestions,
            String userFeedback,
            String marketContext) {

        String prompt = buildImprovementPrompt(originalBrief, previousSuggestions, userFeedback, marketContext);
        try {
            return callClaudeAPI(prompt);
        } catch (Exception e) {
            log.error("Error improving suggestions", e);
            throw new RuntimeException("Failed to improve suggestions: " + e.getMessage(), e);
        }
    }

    /**
     * Count tokens in text
     */
    @Override
    public Integer countTokens(String text) {
        // Rough estimation: 1 token ≈ 4 characters
        return Math.max(1, text.length() / 4);
    }

    /**
     * Estimate cost of LLM call
     */
    @Override
    public BigDecimal estimateCost(Integer inputTokens, Integer outputTokens) {
        if (inputTokens == null || outputTokens == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal inputCost = INPUT_PRICE_PER_1M
                .multiply(BigDecimal.valueOf(inputTokens))
                .divide(BigDecimal.valueOf(1_000_000), 6, java.math.RoundingMode.HALF_UP);

        BigDecimal outputCost = OUTPUT_PRICE_PER_1M
                .multiply(BigDecimal.valueOf(outputTokens))
                .divide(BigDecimal.valueOf(1_000_000), 6, java.math.RoundingMode.HALF_UP);

        return inputCost.add(outputCost);
    }

    /**
     * Validate LLM response
     */
    @Override
    public boolean validateResponse(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        try {
            ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(response);

            // Check required fields
            if (!jsonNode.has("title") || !jsonNode.has("budget")) {
                return false;
            }

            // Check for guaranteed success claims (guardrail)
            if (response.toLowerCase().contains("guaranteed") ||
                response.toLowerCase().contains("100% sure")) {
                return false;
            }

            return true;
        } catch (Exception e) {
            log.warn("Invalid JSON response", e);
            return false;
        }
    }

    /**
     * Get current model
     */
    @Override
    public String getCurrentModel() {
        return model;
    }

    /**
     * Get model pricing
     */
    @Override
    public Map<String, BigDecimal> getModelPricing() {
        Map<String, BigDecimal> pricing = new HashMap<>();
        pricing.put("input_price_per_1m_tokens", INPUT_PRICE_PER_1M);
        pricing.put("output_price_per_1m_tokens", OUTPUT_PRICE_PER_1M);
        return pricing;
    }

    /**
     * Call Claude API with prompt
     */
    private String callClaudeAPI(String prompt) {
        try {
            // Build request payload
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 1500);

            // Add messages
            ArrayNode messagesArray = objectMapper.createArrayNode();
            ObjectNode messageNode = objectMapper.createObjectNode();
            messageNode.put("role", "user");
            messageNode.put("content", prompt);
            messagesArray.add(messageNode);

            requestBody.set("messages", messagesArray);

            // Build HTTP request
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String requestJson = objectMapper.writeValueAsString(requestBody);

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(requestJson, mediaType))
                    .addHeader("x-api-key", apiKey)
                    .addHeader("anthropic-version", "2023-06-01")
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute request
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String errorBody = responseBody != null ? responseBody.string() : "Unknown error";
                    log.error("Claude API error: {} - {}", response.code(), errorBody);
                    throw new RuntimeException("Claude API call failed: " + response.code());
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new RuntimeException("Empty response from Claude API");
                }

                String responseBodyStr = responseBody.string();
                log.debug("Claude API response: {}", responseBodyStr);

                // Parse response
                ObjectNode responseNode = (ObjectNode) objectMapper.readTree(responseBodyStr);
                return responseNode.get("content")
                        .get(0)
                        .get("text")
                        .asText();
            }

        } catch (IOException e) {
            log.error("IOException calling Claude API", e);
            throw new RuntimeException("Failed to call Claude API: " + e.getMessage(), e);
        }
    }

    /**
     * Build prompt for project suggestion
     */
    private String buildProjectSuggestionPrompt(
            String brief,
            String categoryName,
            String scope,
            String timeline,
            List<String> preferredSkills,
            String marketContext) {

        return "You are an expert project advisor for a freelancer marketplace.\n\n" +
                "CRITICAL RULES:\n" +
                "1. BUDGET: Only use data provided below. DO NOT make up prices.\n" +
                "2. OUTPUT: Must be valid JSON with title, description, skills, budget, milestones, clarifyingQuestions.\n" +
                "3. SKILLS: Suggest max 8 skills.\n" +
                "4. NO guarantees: Don't say '100% will win bids'.\n\n" +
                "MARKET CONTEXT:\n" + marketContext + "\n\n" +
                "PROJECT DETAILS:\n" +
                "Category: " + categoryName + "\n" +
                "Scope: " + scope + "\n" +
                "Timeline: " + timeline + "\n" +
                (preferredSkills != null && !preferredSkills.isEmpty()
                    ? "Preferred Skills: " + String.join(", ", preferredSkills) + "\n"
                    : "") +
                "Brief: " + brief + "\n\n" +
                "REQUIRED JSON OUTPUT FORMAT:\n" +
                "{\n" +
                "  \"title\": \"string\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"skills\": [\"skill1\", \"skill2\"],\n" +
                "  \"budget\": {\"min\": 1000, \"recommended\": 2000, \"max\": 3000},\n" +
                "  \"milestones\": [{\"title\": \"string\", \"days\": 7, \"percentage\": 25}],\n" +
                "  \"clarifyingQuestions\": [\"question1\"]\n" +
                "}\n";
    }

    /**
     * Build prompt for improvement
     */
    private String buildImprovementPrompt(
            String originalBrief,
            String previousSuggestions,
            String userFeedback,
            String marketContext) {

        return "You are an expert project advisor. User gave feedback on previous suggestions.\n\n" +
                "ORIGINAL BRIEF:\n" + originalBrief + "\n\n" +
                "PREVIOUS SUGGESTIONS:\n" + previousSuggestions + "\n\n" +
                "USER FEEDBACK:\n" + userFeedback + "\n\n" +
                "MARKET CONTEXT:\n" + marketContext + "\n\n" +
                "Please improve suggestions based on user feedback. Return same JSON format.\n";
    }

    /**
     * Generate chat response for project assistant
     */
    @Override
    public String chat(String userMessage, String projectContext, String projectBrief) {
        String prompt = buildChatPrompt(userMessage, projectContext, projectBrief);
        try {
            return callClaudeAPI(prompt);
        } catch (Exception e) {
            log.error("Error generating chat response", e);
            throw new RuntimeException("Failed to generate chat response: " + e.getMessage(), e);
        }
    }

    /**
     * Build prompt for chat
     */
    private String buildChatPrompt(String userMessage, String projectContext, String projectBrief) {
        return "You are a helpful AI assistant for a freelancer marketplace. You help clients create better project postings.\n\n" +
                "ROLE & BEHAVIOR:\n" +
                "- Answer questions about project setup, budget estimation, skills selection, and best practices.\n" +
                "- Be concise, friendly, and helpful.\n" +
                "- Provide specific, actionable advice.\n" +
                "- If you're unsure about something, say so honestly.\n" +
                "- Don't make up budget figures not supported by context.\n\n" +
                "PROJECT CONTEXT:\n" + projectContext + "\n\n" +
                (projectBrief != null && !projectBrief.isEmpty()
                    ? "ORIGINAL BRIEF:\n" + projectBrief + "\n\n"
                    : "") +
                "USER QUESTION:\n" + userMessage + "\n\n" +
                "IMPORTANT:\n" +
                "- If user asks about budget, use the market data provided in PROJECT CONTEXT.\n" +
                "- If user wants to change something, acknowledge and suggest how to improve.\n" +
                "- Keep responses under 300 words.\n" +
                "- Be supportive and encouraging.\n";
    }
}





