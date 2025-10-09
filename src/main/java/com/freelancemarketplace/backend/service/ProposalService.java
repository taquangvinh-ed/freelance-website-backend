package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProposalDTO;

import java.util.List;

public interface ProposalService {

    ProposalDTO createProposal(Long freelancerId, ProposalDTO proposalDTO);

    ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO);

    void deleteProposal(Long proposalId);

    List<ProposalDTO> getAllProposalByFreelancerId(Long freelancerId);

    List<ProposalDTO> getAllProposalByTeamId(Long teamId);

    void acceptProposal(Long proposalId);

    void rejectProposal(Long proposalId);

    void withdrawProposal(Long proposalId);
}
