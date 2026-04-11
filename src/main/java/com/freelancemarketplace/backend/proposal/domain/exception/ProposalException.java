package com.freelancemarketplace.backend.proposal.domain.exception;

import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;

public class ProposalException extends BaseApplicationException {
    public ProposalException(String message) {
        super(ErrorCode.PROPOSAL_ERROR, message);
    }
}
