package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.domain.model.ContractModel;

public interface ContractLifeCycleService {
    public void startWeeklyReporting(Long contractId);
    public void stopWeeklyReporting(Long contractId);
}
