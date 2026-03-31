package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.dto.BudgetDTO;
import com.freelancemarketplace.backend.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

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
        return ApiResponse.delete();
    }


}
