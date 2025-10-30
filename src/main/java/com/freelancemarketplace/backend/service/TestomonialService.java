package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestomonialService {

    ReviewDTO createReview(ReviewDTO reviewDTO);

    void deleteReview(Long testimonialId);

    Page<ReviewDTO> getAllReviewByFreelancer(Long freelancerId, Pageable pageable);

    Page<ReviewDTO> getAllReviewByTeam(Long teamId, Pageable pageable);

    Page<ReviewDTO> getAllReviewByClient(Long clientId, Pageable pageable);


}
