package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.TestimonialDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestomonialService {

    TestimonialDTO createReview(TestimonialDTO testimonialDTO);

    TestimonialDTO updateReview(Long testimonialId, TestimonialDTO testimonialDTO);

    void deleteReview(Long testimonialId);

    Page<TestimonialDTO> getAllReviewByFreelancer(Long freelancerId, Pageable pageable);

    Page<TestimonialDTO> getAllReviewByTeam(Long teamId, Pageable pageable);

    Page<TestimonialDTO> getAllReviewByClient(Long clientId, Pageable pageable);

    Page<TestimonialDTO> getAllReviewByProject(Long projectId, Pageable pageable);

    Page<TestimonialDTO> getAllReviewByCompany(Long companyId, Pageable pageable);





}
