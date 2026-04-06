package com.freelancemarketplace.backend.proposal.application.service;

import com.freelancemarketplace.backend.proposal.dto.ProjectProposalDTO;
import com.freelancemarketplace.backend.proposal.dto.ProposalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProposalService {

    ProposalDTO createProposal(Long freelancerId, ProposalDTO proposalDTO);

    ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO);

    void deleteProposal(Long proposalId);

    ProposalDTO getProposalById(Long proposalId);

    List<ProposalDTO> getAllProposalByFreelancerId(Long freelancerId);

    List<ProposalDTO> getAllProposalByTeamId(Long teamId);

    Page<ProjectProposalDTO> getAllProposalByProject(Long projectId, Pageable pageable);

    Long approveProposal(Long proposalId);

    void rejectProposal(Long proposalId);

    void withdrawProposal(Long proposalId);

    ProposalDTO getProposalByFreelancerAndProject(Long freelancerId, Long projectId);
}
