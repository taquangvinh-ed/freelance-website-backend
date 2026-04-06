package com.freelancemarketplace.backend.contract.application.service;

import com.freelancemarketplace.backend.contract.domain.model.ContractModel;

public interface ProgressCalculationService {
    public double calculateProgress(ContractModel contract);

    public double calculateFixedPriceProgress(ContractModel contract);

    public double calculateHourlyProgress(ContractModel contract);
}
