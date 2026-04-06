/**
 * Standardized Response Body for /suggest & /improve
 * Liệt kê full schema cố định, KHÔNG dùng { ... }
 */

package com.freelancemarketplace.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSuggestionResponse {

    // ========== TRACING ==========
    private String requestId;  // Unique request ID for tracing

    // ========== PROJECT DRAFT ==========
    private ProjectDraftData projectDraft;

    // ========== BUDGET SUGGESTION ==========
    private BudgetSuggestionData budgetSuggestion;

    // ========== ADVANCED PREFERENCES ==========
    private AdvancedPreferencesData advancedPreferencesSuggestion;

    // ========== CLARIFYING QUESTIONS ==========
    private List<String> clarifyingQuestions;

    // ========== WARNINGS ==========
    private List<String> warnings;

    // ==================== NESTED DTOs ====================

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProjectDraftData {
        private String title;
        private String description;
        private Long categoryId;
        private String scope;  // SMALL, MEDIUM, LARGE, ENTERPRISE
        private List<String> skills;
        private Integer timelineDays;  // Số ngày dự án
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BudgetSuggestionData {
        private String currency;  // USD
        private BigDecimal min;
        private BigDecimal recommended;
        private BigDecimal max;
        private Double confidence;  // 0.0-1.0
        private String pricingSource;  // market_stats_v1
        private String marketSummary;  // Human-readable explanation

        // Budget factors (Full List, không { ... })
        private BudgetFactors factors;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BudgetFactors {
        private Double scopeWeight;        // 1.0
        private Double complexityWeight;   // 0.9-1.1
        private Double experienceWeight;   // 1.0
        private Double urgencyWeight;      // 1.0-1.1
        private Double locationWeight;     // 1.0
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdvancedPreferencesData {
        // Full list - KHÔNG { ... }
        private String experienceLevel;     // BEGINNER, INTERMEDIATE, EXPERT
        private Integer projectDuration;    // Days
        private Integer hoursPerWeek;       // 20
        private String freelancerType;      // INDIVIDUAL, TEAM
        private String locationPreference;  // ANY, SAME_COUNTRY, SAME_TIMEZONE
        private String timezoneOverlap;     // "2-4h"
        private String englishLevel;        // BASIC, CONVERSATIONAL, FLUENT
        private String projectType;         // ONE_TIME, ONGOING, RETAINER
        private String complexity;          // LOW, MEDIUM, HIGH
        private Boolean urgent;             // true/false
    }
}

