package com.freelancemarketplace.backend.recommendation.application.service.imp;

import com.fasterxml.jackson.databind.JsonNode;
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

@Service
@Slf4j
public class LLMServiceImp implements LLMService {

    @Value("${ai.llm.api-key:}")
    private String apiKey;

    @Value("${OPENROUTER_API_KEY:}")
    private String openRouterApiKey;

    @Value("${ai.llm.model:google/gemini-2.5-flash}")
    private String model;

    @Value("${ai.llm.api-url:https://openrouter.ai/api/v1/chat/completions}")
    private String apiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient httpClient;

    private static final BigDecimal INPUT_PRICE_PER_1M = new BigDecimal("0.075");
    private static final BigDecimal OUTPUT_PRICE_PER_1M = new BigDecimal("0.30");

    public LLMServiceImp() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        String resolvedApiKey = resolveApiKey();

        log.info("LLM Config - apiKeyPresent: {}, apiKeyLength: {}, apiKeyPreview: {}, model: {}, apiUrl: {}",
                !resolvedApiKey.isBlank(),
                resolvedApiKey.length(),
                maskApiKey(resolvedApiKey),
                model,
                apiUrl);

        if (resolvedApiKey.isBlank()) {
            log.warn("No OpenRouter API key found. Set OPENROUTER_API_KEY or ai.llm.api-key.");
        }
    }

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
            return callOpenRouterAPI(prompt, resolveApiKey());
        } catch (Exception e) {
            log.error("Error generating project suggestions", e);
            throw new RuntimeException("Failed to generate suggestions: " + e.getMessage(), e);
        }
    }

    @Override
    public String improveProjectSuggestions(
            String originalBrief,
            String previousSuggestions,
            String userFeedback,
            String marketContext) {

        String prompt = buildImprovementPrompt(originalBrief, previousSuggestions, userFeedback, marketContext);
        try {
            return callOpenRouterAPI(prompt, resolveApiKey());
        } catch (Exception e) {
            log.error("Error improving suggestions", e);
            throw new RuntimeException("Failed to improve suggestions: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer countTokens(String text) {
        return Math.max(1, text.length() / 4);
    }

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

    @Override
    public boolean validateResponse(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        try {
            ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(response);

            if (!jsonNode.has("title") || !jsonNode.has("budget")) {
                return false;
            }

            String lowerResponse = response.toLowerCase();
            if (lowerResponse.contains("guaranteed") || lowerResponse.contains("100% sure")) {
                return false;
            }

            return true;
        } catch (Exception e) {
            log.warn("Invalid JSON response", e);
            return false;
        }
    }

    @Override
    public String getCurrentModel() {
        return model;
    }

    @Override
    public Map<String, BigDecimal> getModelPricing() {
        Map<String, BigDecimal> pricing = new HashMap<>();
        pricing.put("input_price_per_1m_tokens", INPUT_PRICE_PER_1M);
        pricing.put("output_price_per_1m_tokens", OUTPUT_PRICE_PER_1M);
        return pricing;
    }

    @Override
    public String chat(String userMessage, String projectContext, String projectBrief) {
        String prompt = buildChatPrompt(userMessage, projectContext, projectBrief);
        try {
            return callOpenRouterAPI(prompt, resolveApiKey());
        } catch (Exception e) {
            log.error("Error generating chat response", e);
            throw new RuntimeException("Failed to generate chat response: " + e.getMessage(), e);
        }
    }

    private String resolveApiKey() {
        String resolved = firstNonBlank(openRouterApiKey, apiKey, System.getenv("OPENROUTER_API_KEY"));
        if (resolved == null) {
            log.warn("All API key sources are null");
            return "";
        }

        String sanitized = resolved.trim().replace("\r", "").replace("\n", "");
        if (isPlaceholder(sanitized)) {
            log.warn("API key is still a placeholder: {}", sanitized);
            return "";
        }

        if (sanitized.length() < 10) {
            log.warn("API key seems invalid (too short): length={}", sanitized.length());
        }

        log.info("Resolved API key - starts with: {}, length: {}",
                sanitized.substring(0, Math.min(10, sanitized.length())), sanitized.length());

        return sanitized;
    }

    private boolean isPlaceholder(String value) {
        return value == null || value.isBlank() || (value.startsWith("${") && value.endsWith("}"));
    }

    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return "<empty>";
        }
        if (apiKey.length() <= 10) {
            return "***";
        }
        return apiKey.substring(0, 6) + "..." + apiKey.substring(apiKey.length() - 4);
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String callOpenRouterAPI(String prompt, String apiKey) {
        try {
            if (isPlaceholder(apiKey)) {
                throw new IllegalStateException("Missing OpenRouter API key. Check OPENROUTER_API_KEY or ai.llm.api-key.");
            }

            ObjectNode requestBody = objectMapper.createObjectNode();

            ArrayNode messagesArray = requestBody.putArray("messages");

            ObjectNode systemMessage = messagesArray.addObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful AI assistant. Be concise and helpful.");

            ObjectNode userMessage = messagesArray.addObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            requestBody.put("model", model);
            requestBody.put("max_tokens", 2048);
            requestBody.put("temperature", 0.7);

            log.info("Calling OpenRouter API with model: {}", model);

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String requestJson = objectMapper.writeValueAsString(requestBody);

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(requestJson, mediaType))
                    .addHeader("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey.trim())
                    .addHeader("HTTP-Referer", "https://freelancer-marketplace.com")
                    .addHeader("X-Title", "Freelancer Marketplace AI Assistant")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String errorBody = responseBody != null ? responseBody.string() : "Unknown error";
                    log.error("OpenRouter API error: {} - {}", response.code(), errorBody);
                    throw new RuntimeException("OpenRouter API call failed: " + response.code() + " - " + errorBody);
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new RuntimeException("Empty response from OpenRouter API");
                }

                String responseBodyStr = responseBody.string();
                log.debug("OpenRouter API response: {}", responseBodyStr);

                JsonNode responseNode = objectMapper.readTree(responseBodyStr);
                return responseNode.path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();
            }

        } catch (IOException e) {
            log.error("IOException calling OpenRouter API", e);
            throw new RuntimeException("Failed to call OpenRouter API: " + e.getMessage(), e);
        }
    }


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
