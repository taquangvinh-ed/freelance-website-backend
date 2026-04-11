package com.freelancemarketplace.backend.review.api.controller;

import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.review.dto.ReviewDTO;
import com.freelancemarketplace.backend.review.application.service.TestomonialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/testimonials", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestimonialController {

    private final TestomonialService testomonialService;

    public TestimonialController(TestomonialService testomonialService) {
        this.testomonialService = testomonialService;
    }


    @PostMapping("/")
    ApiResponse<?> create(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReview = testomonialService.createReview(reviewDTO);
        return ApiResponse.created(newReview);
    }


    @DeleteMapping("/{testimonialId}")
    ApiResponse<?> delete(@PathVariable Long testimonialId
    ) {
        testomonialService.deleteReview(testimonialId);
        return ApiResponse.noContent();
    }

    @GetMapping("/{freelancerId}")
    ApiResponse<?> getAllByFreelancer(@PathVariable Long freelancerId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByFreelancer(freelancerId, pageable);
        return ApiResponse.success(testimonialPage);
    }

    @GetMapping("/{clientId}")
    ApiResponse<?> getAllByClient(@PathVariable Long clientId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByClient(clientId, pageable);
        return ApiResponse.success(testimonialPage);
    }

    @GetMapping("/{teamId}")
    ApiResponse<?> getAllByTeam(@PathVariable Long teamId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByTeam(teamId, pageable);
        return ApiResponse.success(testimonialPage);
    }


}
