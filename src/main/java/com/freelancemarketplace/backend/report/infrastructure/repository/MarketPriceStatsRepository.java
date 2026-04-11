package com.freelancemarketplace.backend.report.infrastructure.repository;

import com.freelancemarketplace.backend.report.domain.model.MarketPriceStatsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketPriceStatsRepository extends JpaRepository<MarketPriceStatsModel, Long> {

    @Query("SELECT m FROM MarketPriceStatsModel m " +
           "WHERE m.category.categoryId = :categoryId " +
           "AND m.scope = :scope " +
           "AND m.experienceLevel = :experienceLevel " +
           "AND COALESCE(m.region, 'Global') = COALESCE(:region, 'Global') " +
           "AND m.isActive = true")
    Optional<MarketPriceStatsModel> findByMarketParams(
            @Param("categoryId") Long categoryId,
            @Param("scope") String scope,
            @Param("experienceLevel") String experienceLevel,
            @Param("region") String region
    );

    @Query("SELECT m FROM MarketPriceStatsModel m " +
           "WHERE m.category.categoryId = :categoryId " +
           "AND m.skill.skillId = :skillId " +
           "AND m.scope = :scope " +
           "AND m.isActive = true")
    Optional<MarketPriceStatsModel> findBySkillAndMarketParams(
            @Param("categoryId") Long categoryId,
            @Param("skillId") Long skillId,
            @Param("scope") String scope
    );

    @Query("SELECT m FROM MarketPriceStatsModel m " +
           "WHERE m.category.categoryId = :categoryId " +
           "AND m.isActive = true ORDER BY m.calculatedAt DESC LIMIT 1")
    Optional<MarketPriceStatsModel> findLatestByCategoryId(@Param("categoryId") Long categoryId);
}

