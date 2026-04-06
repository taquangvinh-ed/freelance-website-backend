package com.freelancemarketplace.backend.project.api.controller;

import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.project.application.service.BudgetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/budgets")
public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ApiResponse<?> createBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO newBudget = budgetService.createBudget(budgetDTO);
        return ApiResponse.created(newBudget);
    }

    @PutMapping("/{budgetId}")
    public ApiResponse<?> updateBudget(@PathVariable Long budgetId,
                                                    @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.updateBudget(budgetId, budgetDTO);
        return ApiResponse.success(updatedBudget);
    }

    @DeleteMapping("/{budgetId}")
    public ApiResponse<?> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ApiResponse.noContent();
    }


}
