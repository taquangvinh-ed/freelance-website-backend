package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.ReviewDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.TestomonialService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/testimonials", produces = {MediaType.APPLICATION_JSON_VALUE})
public class TestimonialController {

    private final TestomonialService testomonialService;

    public TestimonialController(TestomonialService testomonialService) {
        this.testomonialService = testomonialService;
    }


    @PostMapping
    ResponseEntity<ResponseDTO> create(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO newReview = testomonialService.createReview(reviewDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newReview
                ));
    }


    @DeleteMapping("/{testimonialId}")
    ResponseEntity<ResponseDTO> delete(@PathVariable Long testimonialId
    ) {
        testomonialService.deleteReview(testimonialId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }

    @GetMapping("/{freelancerId}")
    ResponseEntity<ResponseDTO> getAllByFreelancer(@PathVariable Long freelancerId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByFreelancer(freelancerId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }

    @GetMapping("/{clientId}")
    ResponseEntity<ResponseDTO> getAllByClient(@PathVariable Long clientId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByClient(clientId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }

    @GetMapping("/{teamId}")
    ResponseEntity<ResponseDTO> getAllByTeam(@PathVariable Long teamId, Pageable pageable
    ) {
        Page<ReviewDTO> testimonialPage = testomonialService.getAllReviewByTeam(teamId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }


}
