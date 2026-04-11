package com.freelancemarketplace.backend.contract.application.service.imp;

import com.freelancemarketplace.backend.toggl.dto.MarketPriceStatsDTO;
import com.freelancemarketplace.backend.report.domain.model.MarketPriceStatsModel;
import com.freelancemarketplace.backend.project.infrastructure.repository.BudgetsRepository;
import com.freelancemarketplace.backend.report.infrastructure.repository.MarketPriceStatsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.contract.application.service.PricingEngineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of Pricing Engine Service
 * Calculates market-based budget recommendations from historical project data
 */
@Service
@AllArgsConstructor
@Slf4j
public class PricingEngineServiceImp implements PricingEngineService {

    private final ProjectsRepository projectsRepository;
    private final BudgetsRepository budgetRepository;
    private final MarketPriceStatsRepository marketPriceStatsRepository;

    /**
     * Calculate market statistics for a given set of project parameters
     * Uses caching to avoid expensive DB queries
     */
    @Override
    @Cacheable(value = "marketStats", key = "#categoryId + ':' + #scope + ':' + #experienceLevel + ':' + #region")
    public MarketPriceStatsDTO calculateMarketStats(
            Long categoryId,
            String scope,
            String experienceLevel,
            String region) {

        log.info("Calculating market stats for category={}, scope={}, level={}, region={}",
                categoryId, scope, experienceLevel, region);

        // Check if we have cached stats in DB
        var cachedStats = marketPriceStatsRepository.findByMarketParams(
                categoryId, scope, experienceLevel, region != null ? region : "Global");

        if (cachedStats.isPresent() && isStatsFresh(cachedStats.get())) {
            log.debug("Using cached market stats");
            return mapToDTO(cachedStats.get());
        }

        // Calculate fresh stats from completed projects
        // This is a simplified calculation - in production, use PostgreSQL window functions
        var priceStats = calculateFromProjects(categoryId, scope, experienceLevel, region);

        return priceStats;
    }

    /**
     * Calculate percentiles from historical project data
     * In production, this should use native SQL with percentile functions
     */
    private MarketPriceStatsDTO calculateFromProjects(Long categoryId, String scope, String experienceLevel, String region) {
        // TODO: Implement native SQL query using PostgreSQL percentile functions
        // SELECT
        //   PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY maxValue) as p25,
        //   PERCENTILE_CONT(0.50) WITHIN GROUP (ORDER BY maxValue) as p50,
        //   PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY maxValue) as p75,
        //   COUNT(*) as sample_count
        // FROM Budgets b
        // JOIN Projects p ON b.projectId = p.projectId
        // WHERE p.categoryId = ? AND p.scope = ? AND p.status = 'COMPLETED'

        MarketPriceStatsDTO stats = new MarketPriceStatsDTO();
        stats.setCategoryId(categoryId);
        stats.setScope(scope);
        stats.setExperienceLevel(experienceLevel);
        stats.setRegion(region != null ? region : "Global");

        // Temporary default values (replace with actual calculation)
        return switch (scope) {
            case "SMALL" -> {
                stats.setP25Budget(BigDecimal.valueOf(200));
                stats.setP50Budget(BigDecimal.valueOf(500));
                stats.setP75Budget(BigDecimal.valueOf(1000));
                yield stats;
            }
            case "MEDIUM" -> {
                stats.setP25Budget(BigDecimal.valueOf(1000));
                stats.setP50Budget(BigDecimal.valueOf(3000));
                stats.setP75Budget(BigDecimal.valueOf(6000));
                yield stats;
            }
            case "LARGE" -> {
                stats.setP25Budget(BigDecimal.valueOf(5000));
                stats.setP50Budget(BigDecimal.valueOf(12000));
                stats.setP75Budget(BigDecimal.valueOf(25000));
                yield stats;
            }
            default -> { // ENTERPRISE
                stats.setP25Budget(BigDecimal.valueOf(25000));
                stats.setP50Budget(BigDecimal.valueOf(50000));
                stats.setP75Budget(BigDecimal.valueOf(100000));
                yield stats;
            }
        };
    }

    /**
     * Check if cached stats are still fresh (< 24 hours)
     */
    private boolean isStatsFresh(MarketPriceStatsModel stats) {
        if (stats.getCalculatedAt() == null) return false;
        long ageMs = System.currentTimeMillis() - stats.getCalculatedAt().getTime();
        return ageMs < 24 * 60 * 60 * 1000; // 24 hours
    }

    /**
     * Apply complexity factor to base budget
     * Complexity factors: LOW (0.8), MEDIUM (1.0), HIGH (1.3)
     */
    @Override
    public BigDecimal applyComplexityFactor(BigDecimal baseMedian, String complexity) {
        if (baseMedian == null) return null;

        BigDecimal factor = BigDecimal.ONE;
        if ("LOW".equalsIgnoreCase(complexity)) {
            factor = new BigDecimal("0.8");
        } else if ("MEDIUM".equalsIgnoreCase(complexity)) {
            factor = new BigDecimal("1.0");
        } else if ("HIGH".equalsIgnoreCase(complexity)) {
            factor = new BigDecimal("1.3");
        }

        return baseMedian.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Apply urgency factor to base budget
     * Urgency factors:
     * - Less than 1 month: 1.5x (rush fee)
     * - 1 to 3 months: 1.0x (normal)
     * - 3 to 6 months: 0.95x (slight discount)
     * - More than 6 months: 0.85x (good discount)
     */
    @Override
    public BigDecimal applyUrgencyFactor(BigDecimal baseMedian, String timeline) {
        if (baseMedian == null) return null;

        BigDecimal factor = BigDecimal.ONE;
        if ("Less than 1 month".equalsIgnoreCase(timeline)) {
            factor = new BigDecimal("1.5");
        } else if ("1 to 3 months".equalsIgnoreCase(timeline)) {
            factor = new BigDecimal("1.0");
        } else if ("3 to 6 months".equalsIgnoreCase(timeline)) {
            factor = new BigDecimal("0.95");
        } else if ("More than 6 months".equalsIgnoreCase(timeline)) {
            factor = new BigDecimal("0.85");
        }

        return baseMedian.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void recalculateMarketStats(Long categoryId) {
        log.info("Recalculating market stats for category={}", categoryId);
        // TODO: Implement full recalculation from projects
        // Delete old cached stats
        // Query all completed projects for this category
        // Calculate percentiles
        // Store in DB
    }

    @Override
    public void refreshAllMarketStats() {
        log.info("Refreshing all market statistics");
        // TODO: Called by scheduled task (e.g., daily)
        // Get all unique (categoryId, scope, experienceLevel, region) combinations
        // Recalculate each one
    }

    /**
     * Calculate confidence score based on sample size
     * More samples = higher confidence
     */
    @Override
    public Double getConfidenceScore(Integer sampleCount) {
        if (sampleCount == null || sampleCount < 1) return 0.0;
        if (sampleCount < 5) return 0.3;
        if (sampleCount < 10) return 0.5;
        if (sampleCount < 20) return 0.7;
        if (sampleCount < 50) return 0.85;
        return 0.95; // Max confidence at 50+ samples
    }

    private MarketPriceStatsDTO mapToDTO(MarketPriceStatsModel model) {
        MarketPriceStatsDTO dto = new MarketPriceStatsDTO();
        dto.setCategoryId(model.getCategory().getCategoryId());
        dto.setScope(model.getScope());
        dto.setExperienceLevel(model.getExperienceLevel());
        dto.setRegion(model.getRegion());
        dto.setP25Budget(model.getP25Budget());
        dto.setP50Budget(model.getP50Budget());
        dto.setP75Budget(model.getP75Budget());
        dto.setSampleCount(model.getSampleCount());
        dto.setConfidence(model.getConfidence());
        dto.setCurrency(model.getCurrency());
        return dto;
    }
}

