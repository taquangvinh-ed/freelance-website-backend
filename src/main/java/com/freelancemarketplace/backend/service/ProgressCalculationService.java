package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.domain.model.ContractModel;

public interface ProgressCalculationService {
    public double calculateProgress(ContractModel contract);

    public double calculateFixedPriceProgress(ContractModel contract);

    public double calculateHourlyProgress(ContractModel contract);
}
