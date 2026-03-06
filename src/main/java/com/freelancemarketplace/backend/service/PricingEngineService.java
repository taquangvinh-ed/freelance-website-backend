package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.MarketPriceStatsDTO;

import java.math.BigDecimal;

/**
 * Pricing Engine Service
 * Calculates market-based budget recommendations from historical project data
 * Does NOT use LLM for pricing - uses only real marketplace data
 */
public interface PricingEngineService {

    /**
     * Calculate market statistics for a given set of project parameters
     * @param categoryId Category of the project
     * @param scope SMALL, MEDIUM, LARGE, ENTERPRISE
     * @param experienceLevel BEGINNER, INTERMEDIATE, EXPERT
     * @param region Optional: for regional adjustment
     * @return Market price statistics with p25, p50, p75
     */
    MarketPriceStatsDTO calculateMarketStats(
            Long categoryId,
            String scope,
            String experienceLevel,
            String region
    );

    /**
     * Apply complexity factor to base budget
     * @param baseMedian Base median budget (p50)
     * @param complexity LOW, MEDIUM, HIGH
     * @return Adjusted budget amount
     */
    BigDecimal applyComplexityFactor(BigDecimal baseMedian, String complexity);

    /**
     * Apply urgency factor to base budget
     * @param baseMedian Base median budget
     * @param timeline Less than 1 month, 1 to 3 months, 3 to 6 months, More than 6 months
     * @return Adjusted budget amount
     */
    BigDecimal applyUrgencyFactor(BigDecimal baseMedian, String timeline);

    /**
     * Recalculate market statistics from scratch (expensive operation, should be cached)
     * @param categoryId Category to recalculate for
     */
    void recalculateMarketStats(Long categoryId);

    /**
     * Refresh all market statistics (should be run periodically, e.g., daily)
     */
    void refreshAllMarketStats();

    /**
     * Get confidence score based on sample size
     * @param sampleCount Number of projects used in calculation
     * @return Confidence score 0.0-1.0
     */
    Double getConfidenceScore(Integer sampleCount);
}

