package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.FreelancerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/freelancer", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }


    @PostMapping("/")
    ResponseEntity<ResponseDTO>createFreelancer(@RequestBody FreelancerDTO freelancerDTO){
        FreelancerDTO newFreelancerDTO = freelancerService.createFreelancer(freelancerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newFreelancerDTO
                ));
    }

    @PutMapping("/{freelancerId}")
    ResponseEntity<ResponseDTO> updateFreelancer(@PathVariable Long freelancerId,
                                                @RequestBody FreelancerDTO freelancerDTO){
        FreelancerDTO updatedFreelancer = freelancerService.updateFreelancer(freelancerId, freelancerDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedFreelancer
                ));
    }
}
