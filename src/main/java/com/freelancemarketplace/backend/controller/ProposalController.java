package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ProposalDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.ProposalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/proposals", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
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

    @PutMapping("/{proposalId}")
    public ResponseEntity<ResponseDTO>updatedProposal(@PathVariable Long proposalId,
                                                      @RequestBody ProposalDTO proposalDTO){
        ProposalDTO updatedProposal = proposalService.updateProposal(proposalId, proposalDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedProposal
                ));
    }


    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDTO>deleteProposal(@PathVariable Long proposalId){
        proposalService.deleteProposal(proposalId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(
                        ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }


    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<ResponseDTO>getAllProposalByFreelancerId(@PathVariable Long freelancerId){

        List<ProposalDTO> proposals = proposalService.getAllProposalByFreelancerId(freelancerId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        proposals
                ));
    }

    @GetMapping("/team/{teaId}")
    public ResponseEntity<ResponseDTO>updatedProposal(@PathVariable Long teamId){

        List<ProposalDTO> proposals = proposalService.getAllProposalByTeamId(teamId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        proposals
                ));
    }

    @PutMapping("/{proposalId}/accept")
    public ResponseEntity<ResponseDTO>acceptProposal(@PathVariable Long proposalId){
        proposalService.acceptProposal(proposalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));
    }

    @PutMapping("/{proposalId}/reject")
    public ResponseEntity<ResponseDTO>rejectProposal(@PathVariable Long proposalId){
        proposalService.rejectProposal(proposalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));
    }

    @PutMapping("/{proposalId}/withdraw")
    public ResponseEntity<ResponseDTO>withdrawProposal(@PathVariable Long proposalId){
        proposalService.withdrawProposal(proposalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                ));
    }



}
