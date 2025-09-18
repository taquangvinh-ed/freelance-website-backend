package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.BudgetDTO;

public interface BudgetService {


    BudgetDTO createBudget(BudgetDTO budgetDTO);

    BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO);

    void deleteBudget(Long budgetId);

}
