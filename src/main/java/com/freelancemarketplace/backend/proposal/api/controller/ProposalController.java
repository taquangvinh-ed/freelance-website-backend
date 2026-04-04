package com.freelancemarketplace.backend.proposal.api.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.contract.dto.ContractDTO;
import com.freelancemarketplace.backend.proposal.dto.ProjectProposalDTO;
import com.freelancemarketplace.backend.proposal.dto.ProposalDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.proposal.application.service.ProposalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/proposals", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping("/")
    public ApiResponse<?> createProposal(@AuthenticationPrincipal AppUser appUser, @RequestBody ProposalDTO proposalDTO){

    Long userId = appUser.getId();

        ProposalDTO newProposal = proposalService.createProposal(userId, proposalDTO);
        return ApiResponse.created(newProposal);
    }

    @PatchMapping("/{proposalId}")
    public ApiResponse<?> updatedProposal(@PathVariable Long proposalId,
                                                      @RequestBody ProposalDTO proposalDTO){
        ProposalDTO updatedProposal = proposalService.updateProposal(proposalId, proposalDTO);
        return ApiResponse.success(updatedProposal);
    }


    @DeleteMapping("/{proposalId}")
    public ApiResponse<?> deleteProposal(@PathVariable Long proposalId){
        proposalService.deleteProposal(proposalId);
        return ApiResponse.noContent();
    }


    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalDTO>getProposalById(@PathVariable Long proposalId){
       ProposalDTO proposal =  proposalService.getProposalById(proposalId);
        return ResponseEntity.ok(proposal);
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ApiResponse<?> getAllProposalByFreelancerId(@PathVariable Long freelancerId){

        List<ProposalDTO> proposals = proposalService.getAllProposalByFreelancerId(freelancerId);

        return ApiResponse.success(proposals);
    }

    @GetMapping("/team/{teaId}")
    public ApiResponse<?> updatedProposal(@PathVariable Long teamId){

        List<ProposalDTO> proposals = proposalService.getAllProposalByTeamId(teamId);

        return ApiResponse.success(proposals);
    }

    @PatchMapping("/{proposalId}/approve")
    public ResponseEntity<Long>acceptProposal(@PathVariable Long proposalId){
        Long contractId = proposalService.approveProposal(proposalId);
        return ResponseEntity.ok(contractId);
    }

    @PutMapping("/{proposalId}/reject")
    public ApiResponse<?> rejectProposal(@PathVariable Long proposalId){
        proposalService.rejectProposal(proposalId);
        return ApiResponse.noContent();
    }

    @PutMapping("/{proposalId}/withdraw")
    public ApiResponse<?> withdrawProposal(@PathVariable Long proposalId){
        proposalService.withdrawProposal(proposalId);
        return ApiResponse.noContent();
    }

    @GetMapping("/project/{projectId}")
    ResponseEntity<Page<ProjectProposalDTO>> getAllProposalByProject(@PathVariable Long projectId, Pageable pageable){
        Page<ProjectProposalDTO> proposalPage = proposalService.getAllProposalByProject(projectId, pageable);
        return ResponseEntity.ok(proposalPage);
    }

    @GetMapping("/find-by-freelancer-and-project/{projectId}")
    ResponseEntity<ProposalDTO> findProposalByFreelancerAndProject(@AuthenticationPrincipal AppUser appUser,
                                                                   @PathVariable Long projectId){
        Long userId = appUser.getId();
        ProposalDTO proposal = proposalService.getProposalByFreelancerAndProject(userId, projectId);
        return ResponseEntity.ok(proposal);
    }


}
