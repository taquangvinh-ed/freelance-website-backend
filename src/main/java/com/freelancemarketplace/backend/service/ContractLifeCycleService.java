package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.model.ContractModel;

public interface ContractLifeCycleService {
    public void startWeeklyReporting(Long contractId);
    public void stopWeeklyReporting(Long contractId);
}
