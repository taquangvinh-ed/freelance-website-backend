package com.freelancemarketplace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for market price statistics
 * Used internally for pricing engine calculations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketPriceStatsDTO {

    private Long categoryId;

    private Long skillId;

    private String scope; // SMALL, MEDIUM, LARGE

    private String experienceLevel; // BEGINNER, INTERMEDIATE, EXPERT

    private String region;

    // Percentile values
    private BigDecimal p25Budget; // 25th percentile

    private BigDecimal p50Budget; // 50th percentile (median)

    private BigDecimal p75Budget; // 75th percentile

    private Integer sampleCount; // How many projects were included in calculation

    private Double confidence; // Confidence score (0.0 to 1.0) based on sample size

    private String currency;
}

