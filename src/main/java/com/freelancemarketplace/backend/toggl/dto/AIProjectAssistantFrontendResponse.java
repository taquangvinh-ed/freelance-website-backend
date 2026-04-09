package com.freelancemarketplace.backend.toggl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIProjectAssistantFrontendResponse {

    private boolean success;
    private String message;
    private String timestamp;
    private Body body;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        private String requestId;
        private ProjectDraft projectDraft;
        private BudgetSuggestion budgetSuggestion;
        private AdvancedPreferencesSuggestion advancedPreferencesSuggestion;
        private List<String> clarifyingQuestions;
        private List<String> warnings;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDraft {
        private String title;
        private String description;
        private Long categoryId;
        private String scope;
        private List<String> skills;
        private Integer timelineDays;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetSuggestion {
        private String currency;
        private BigDecimal min;
        private BigDecimal recommended;
        private BigDecimal max;
        private Double confidence;
        private String pricingSource;
        private String marketSummary;
        private Map<String, Double> factors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdvancedPreferencesSuggestion {
        private String experienceLevel;
        private Integer projectDuration;
        private Integer hoursPerWeek;
        private String freelancerType;
        private String locationPreference;
        private String timezoneOverlap;
        private String englishLevel;
        private String projectType;
        private String complexity;
        private boolean urgent;
    }
}

