package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.toggl.dto.MarketPriceStatsDTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Application Service: Pricing Engine (SRP - Single Responsibility Principle)
 * This service handles ONLY market price calculation and suggestions
 */
public interface PricingEngineService {
    
    /**
     * Calculate market statistics for project pricing
     * @param categoryId Category ID
     * @param scope Project scope
     * @param experienceLevel Experience level required
     * @param region Region/Location
     * @return Market statistics with min/median/max prices
     */
    MarketPriceStatsDTO calculateMarketStats(
            Long categoryId,
            String scope,
            String experienceLevel,
            String region
    );
    
    /**
     * Apply complexity factor to base budget
     * Complexity factors: LOW (0.8), MEDIUM (1.0), HIGH (1.3)
     */
    BigDecimal applyComplexityFactor(BigDecimal baseMedian, String complexity);
    
    /**
     * Apply urgency factor to base budget
     * Urgency factors:
     * - Less than 1 month: 1.5x (rush fee)
     * - 1 to 3 months: 1.0x (normal)
     * - 3 to 6 months: 0.95x (slight discount)
     * - More than 6 months: 0.85x (good discount)
     */
    BigDecimal applyUrgencyFactor(BigDecimal baseMedian, String timeline);
    
    /**
     * Recalculate market stats for a category
     */
    void recalculateMarketStats(Long categoryId);
    
    /**
     * Refresh all market statistics
     */
    void refreshAllMarketStats();
    
    /**
     * Calculate confidence score based on sample size
     * More samples = higher confidence
     */
    Double getConfidenceScore(Integer sampleCount);
}
