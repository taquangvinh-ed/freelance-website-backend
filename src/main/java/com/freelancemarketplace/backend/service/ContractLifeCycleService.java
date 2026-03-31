package com.freelancemarketplace.backend.service;

public interface ContractLifeCycleService {
    public void startWeeklyReporting(Long contractId);
    public void stopWeeklyReporting(Long contractId);
}
