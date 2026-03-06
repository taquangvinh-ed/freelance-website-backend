package com.freelancemarketplace.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Caches market price statistics calculated from historical projects
 * Updated periodically (e.g., daily/weekly)
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "market_price_stats",
       uniqueConstraints = @UniqueConstraint(columnNames = {"categoryId", "scope", "experienceLevel", "region"}))
public class MarketPriceStatsModel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private CategoryModel category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skillId")
    private SkillModel skill; // Optional: for skill-specific pricing

    private String scope; // SMALL, MEDIUM, LARGE, ENTERPRISE

    private String experienceLevel; // BEGINNER, INTERMEDIATE, EXPERT

    private String region; // e.g., "Vietnam", "USA", "Global"

    // Calculated percentiles
    private BigDecimal p25Budget;

    private BigDecimal p50Budget; // Median

    private BigDecimal p75Budget;

    private Integer sampleCount; // How many projects were included

    private String currency; // e.g., "USD", "VND"

    // Confidence calculation
    private Double confidence; // Based on sample size (0.0 to 1.0)

    private Timestamp calculatedAt; // When this was last calculated

    private Boolean isActive; // Whether to use this in recommendations
}

