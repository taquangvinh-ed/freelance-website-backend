package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ProposalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/proposals", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProposalRepository {

    private final ProposalService proposalService;

    public ProposalRepository(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO>createProposal(@RequestBody ProposalDTO proposalDTO){
        ProposalDTO newProposal = proposalService.createProposal(proposalDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newProposal
                ));
    }
}
