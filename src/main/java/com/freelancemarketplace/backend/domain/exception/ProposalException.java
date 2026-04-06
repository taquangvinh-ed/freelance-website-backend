package com.freelancemarketplace.backend.domain.exception;

public class ProposalException extends BaseApplicationException {
    public ProposalException(String message) {
        super(ErrorCode.PROPOSAL_ERROR, message);
    }
}
