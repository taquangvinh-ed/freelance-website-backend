package com.freelancemarketplace.backend.contract.exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String message) {
        super(message);
    }
}

