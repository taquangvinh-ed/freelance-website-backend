package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.TestimonialDTO;
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
    ResponseEntity<ResponseDTO> create(@RequestBody TestimonialDTO testimonialDTO){
        TestimonialDTO newTestimonial = testomonialService.createReview(testimonialDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newTestimonial
                ));
    }

    @PutMapping("/{testimonialId}")
    ResponseEntity<ResponseDTO> update(@PathVariable Long testimonialId,
                                       @RequestBody TestimonialDTO testimonialDTO){
        TestimonialDTO updatedTestimonial = testomonialService.updateReview(testimonialId,testimonialDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedTestimonial
                ));
    }

    @DeleteMapping("/{testimonialId}")
    ResponseEntity<ResponseDTO> delete(@PathVariable Long testimonialId
                                       ){
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
    ){
        Page<TestimonialDTO> testimonialPage  = testomonialService.getAllReviewByFreelancer(freelancerId, pageable);
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
    ){
        Page<TestimonialDTO> testimonialPage  = testomonialService.getAllReviewByClient(clientId, pageable);
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
    ){
        Page<TestimonialDTO> testimonialPage  = testomonialService.getAllReviewByTeam(teamId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }

    @GetMapping("/{companyId}")
    ResponseEntity<ResponseDTO> getAllByCompany(@PathVariable Long companyId, Pageable pageable
    ){
        Page<TestimonialDTO> testimonialPage  = testomonialService.getAllReviewByCompany(companyId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }

    @GetMapping("/{projectId}")
    ResponseEntity<ResponseDTO> getAllByProject(@PathVariable Long projectId, Pageable pageable
    ){
        Page<TestimonialDTO> testimonialPage  = testomonialService.getAllReviewByCompany(projectId, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        testimonialPage
                ));
    }


}
