package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Stores AI-generated project recommendations for audit and learning purposes
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ai_project_recommendations")
public class AIProjectRecommendationModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", nullable = false)
    private ClientModel client;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String userBrief;

    // Suggested content
    private String suggestedTitle;

    @Column(columnDefinition = "TEXT")
    private String suggestedDescription;

    // Suggested budget
    private BigDecimal suggestedBudgetMin;

    private BigDecimal suggestedBudgetRecommended;

    private BigDecimal suggestedBudgetMax;

    private BigDecimal budgetConfidence; // 0.0 to 1.0

    // Factors applied
    private BigDecimal complexityFactor;

    private BigDecimal urgencyFactor;

    // Market context
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private CategoryModel category;

    private String experienceLevel;

    private String region;

    private Integer marketSampleCount; // How many projects were used for market stats

    private String marketContext; // e.g., "Based on 45 similar projects in Web Development"

    // User feedback
    @Enumerated(EnumType.STRING)
    private RecommendationFeedback userFeedback; // ACCEPTED, REJECTED, PARTIAL, NOT_PROVIDED

    private String feedbackNotes;

    private Integer tokenCount; // For cost analysis

    private BigDecimal estimatedCost; // Cost of this AI API call

    @Column(columnDefinition = "TEXT")
    private String rawJsonResponse; // Store raw LLM response for debugging

    private String llmModel; // Which LLM model was used (gpt-4o, claude-3-haiku, etc)

    private Boolean isActive; // Whether this recommendation is still relevant

    public enum RecommendationFeedback {
        ACCEPTED,      // User accepted all suggestions
        REJECTED,      // User rejected all suggestions
        PARTIAL,       // User accepted some suggestions
        NOT_PROVIDED   // User hasn't provided feedback yet
    }
}

