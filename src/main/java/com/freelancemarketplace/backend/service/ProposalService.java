package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ProposalDTO;

public interface ProposalService {

    ProposalDTO createProposal(ProposalDTO proposalDTO);

    ProposalDTO updateProposal(Long proposalId, ProposalDTO proposalDTO);

    ProposalDTO deleteProposal(Long proposalId);

}
