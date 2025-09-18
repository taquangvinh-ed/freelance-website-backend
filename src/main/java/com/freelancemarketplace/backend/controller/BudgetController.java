package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.BudgetDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/budgets")
public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO newBudget = budgetService.createBudget(budgetDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newBudget));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<ResponseDTO> updateBudget(@PathVariable Long budgetId,
                                                    @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.updateBudget(budgetId, budgetDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedBudget));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<ResponseDTO> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT
                ));
    }


}
