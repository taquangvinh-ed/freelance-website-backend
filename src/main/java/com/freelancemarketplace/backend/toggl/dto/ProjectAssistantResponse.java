package com.freelancemarketplace.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for AI Project Assistant
 * Contains AI-generated suggestions for project setup
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAssistantResponse {

    @JsonProperty("title")
    private String suggestedTitle;

    @JsonProperty("description")
    private String suggestedDescription;

    @JsonProperty("skills")
    private List<SkillSuggestion> suggestedSkills;

    @JsonProperty("budget")
    private BudgetSuggestion budgetSuggestion;

    @JsonProperty("milestones")
    private List<MilestoneSuggestion> suggestedMilestones;

    @JsonProperty("clarifyingQuestions")
    private List<String> clarifyingQuestions;

    @JsonProperty("confidence")
    private Double overallConfidence; // 0.0 to 1.0

    @JsonProperty("reasoning")
    private String reasoning; // Explanation of why these suggestions were made

    /**
     * Nested class for budget suggestion
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetSuggestion {
        @JsonProperty("min")
        private BigDecimal minBudget;

        @JsonProperty("recommended")
        private BigDecimal recommendedBudget;

        @JsonProperty("max")
        private BigDecimal maxBudget;

        @JsonProperty("confidence")
        private Double confidence; // How confident is the AI in this budget range

        @JsonProperty("explanation")
        private String explanation; // Why this budget range (e.g., based on market data)

        @JsonProperty("marketContext")
        private String marketContext; // e.g., "Based on 45 similar projects in Web Development"
    }

    /**
     * Nested class for skill suggestion
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillSuggestion {
        @JsonProperty("name")
        private String skillName;

        @JsonProperty("priority")
        private String priority; // HIGH, MEDIUM, LOW

        @JsonProperty("reason")
        private String reason; // Why this skill is recommended
    }

    /**
     * Nested class for milestone suggestion
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MilestoneSuggestion {
        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("daysFromStart")
        private Integer daysFromStart;

        @JsonProperty("budgetPercentage")
        private Integer budgetPercentage; // Percentage of total budget
    }
}

