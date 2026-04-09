package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.domain.model.AIProjectRecommendationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIProjectRecommendationRepository extends JpaRepository<AIProjectRecommendationModel, Long> {

    @Query("SELECT ar FROM AIProjectRecommendationModel ar WHERE ar.client.clientId = :clientId " +
           "ORDER BY ar.createdAt DESC")
    Page<AIProjectRecommendationModel> findByClientIdOrderByCreatedAtDesc(@Param("clientId") Long clientId, Pageable pageable);

    @Query("SELECT ar FROM AIProjectRecommendationModel ar WHERE ar.client.clientId = :clientId " +
           "ORDER BY ar.createdAt DESC")
    List<AIProjectRecommendationModel> findByClientIdOrderByCreatedAtDesc(@Param("clientId") Long clientId);

    @Query("SELECT ar FROM AIProjectRecommendationModel ar WHERE ar.client.clientId = :clientId " +
           "AND ar.userFeedback IS NULL ORDER BY ar.createdAt DESC")
    List<AIProjectRecommendationModel> findPendingFeedbackByClientId(@Param("clientId") Long clientId);

    @Query("SELECT COUNT(ar) FROM AIProjectRecommendationModel ar " +
           "WHERE ar.client.clientId = :clientId AND ar.createdAt >= :timeLimit")
    Integer countByClientIdAndRecentCreation(@Param("clientId") Long clientId, @Param("timeLimit") java.sql.Timestamp timeLimit);

    @Query("SELECT ar.userFeedback, COUNT(ar) FROM AIProjectRecommendationModel ar " +
           "WHERE ar.category.categoryId = :categoryId GROUP BY ar.userFeedback")
    List<Object[]> getStatsByCategory(@Param("categoryId") Long categoryId);
}

