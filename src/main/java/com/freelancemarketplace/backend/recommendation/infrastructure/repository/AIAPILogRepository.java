package com.freelancemarketplace.backend.recommendation.infrastructure.repository;

import com.freelancemarketplace.backend.recommendation.domain.model.AIAPILogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface AIAPILogRepository extends JpaRepository<AIAPILogModel, Long> {

    @Query("SELECT a FROM AIAPILogModel a WHERE a.user.userId = :userId ORDER BY a.timestamp DESC")
    List<AIAPILogModel> findByUserId(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT SUM(a.estimatedCost) FROM AIAPILogModel a WHERE a.user.userId = :userId AND a.timestamp >= :startDate")
    BigDecimal sumCostByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Timestamp startDate);

    @Query("SELECT COUNT(a) FROM AIAPILogModel a WHERE a.user.userId = :userId AND a.timestamp >= :timeLimit")
    Integer countCallsByUserIdAndTimeLimit(@Param("userId") Long userId, @Param("timeLimit") Timestamp timeLimit);

    @Query("SELECT a.model, COUNT(a), SUM(a.estimatedCost) FROM AIAPILogModel a " +
           "WHERE a.timestamp >= :startDate GROUP BY a.model")
    List<Object[]> getModelUsageStats(@Param("startDate") Timestamp startDate);

    @Query("SELECT a FROM AIAPILogModel a WHERE a.responseStatus = 'ERROR' ORDER BY a.timestamp DESC")
    List<AIAPILogModel> findErrorLogs(org.springframework.data.domain.Pageable pageable);
}

