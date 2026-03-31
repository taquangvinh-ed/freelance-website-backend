package com.freelancemarketplace.backend.application.service;

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
    Map<String, Object> calculateMarketStats(
            Long categoryId,
            String scope,
            String experienceLevel,
            String region
    );
    
    /**
     * Get suggested budget based on market data and complexity
     * @param baseMedianPrice Base median price from market
     * @param complexityFactor Complexity multiplier (1.0 = normal)
     * @param urgencyFactor Urgency multiplier
     * @return Suggested budget with min/median/max
     */
    Map<String, Object> suggestBudget(
            BigDecimal baseMedianPrice,
            Double complexityFactor,
            Double urgencyFactor
    );
    
    /**
     * Get price percentiles for market
     * @param categoryId Category ID
     * @param scope Project scope
     * @return Map with p25, p50, p75 percentiles
     */
    Map<String, BigDecimal> getPricePercentiles(Long categoryId, String scope);
}

