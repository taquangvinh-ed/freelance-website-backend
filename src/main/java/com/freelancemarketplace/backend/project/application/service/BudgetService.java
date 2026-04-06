package com.freelancemarketplace.backend.project.application.service;

import com.freelancemarketplace.backend.project.dto.BudgetDTO;

public interface BudgetService {


    BudgetDTO createBudget(BudgetDTO budgetDTO);

    BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO);

    void deleteBudget(Long budgetId);

}
