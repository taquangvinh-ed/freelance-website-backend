package com.freelancemarketplace.backend.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Logs all AI API calls for cost analysis, monitoring, and rate limiting
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ai_api_logs")
public class AIAPILogModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private com.freelancemarketplace.backend.user.domain.model.UserModel user; // Who made this request

    private String provider; // anthropic, openai, google

    private String model; // e.g., claude-3-haiku, gpt-4o, gemini-1.5

    private Integer promptTokens; // Input tokens

    private Integer completionTokens; // Output tokens

    private Integer totalTokens; // Total

    private BigDecimal estimatedCost; // Calculated cost based on pricing

    private String responseStatus; // SUCCESS, ERROR, TIMEOUT, etc

    private String errorMessage; // If error occurred

    private Long responseTimeMs; // How long the API call took

    private String feature; // Which feature called this (e.g., "project_assistant")

    private Boolean cached; // Was this result cached?

    private Timestamp timestamp;
}

