package com.freelancemarketplace.backend.project.application.service.imp;

import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.project.exception.BudgetNotFoundException;
import com.freelancemarketplace.backend.project.exception.ProjectNotFoundException;
import com.freelancemarketplace.backend.project.infrastructure.mapper.BudgetMapper;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.infrastructure.repository.BudgetsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.project.application.service.BudgetService;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImp implements BudgetService {

    private final BudgetsRepository budgetsRepository;
    private final ProjectsRepository projectsRepository;
    private final BudgetMapper budgetMapper;

    public BudgetServiceImp(BudgetsRepository budgetsRepository, ProjectsRepository projectsRepository, BudgetMapper budgetMapper) {
        this.budgetsRepository = budgetsRepository;
        this.projectsRepository = projectsRepository;
        this.budgetMapper = budgetMapper;
    }

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        BudgetModel newbudget = budgetMapper.toEntity(budgetDTO);
        ProjectModel project = projectsRepository.findById(budgetDTO.getProjectId()).orElseThrow(
                () -> new ProjectNotFoundException("Project with id: " + budgetDTO.getProjectId() + " not found")
        );

        newbudget.setProject(project);
        BudgetModel savedBudget = budgetsRepository.save(newbudget);
        return budgetMapper.toDto(savedBudget);
    }



    @Override
    public BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO) {
        BudgetModel budget = budgetsRepository.findById(budgetId).orElseThrow(
                ()->new BudgetNotFoundException("Budget with id: " + budgetId + " not found")
        );
        BudgetModel updatedBudget = budgetMapper.partialUpdate(budgetDTO, budget);
        BudgetModel savedBudget = budgetsRepository.save(updatedBudget);
        return budgetMapper.toDto(savedBudget);
    }

    @Override
    public void deleteBudget(Long budgetId) {
        if(!budgetsRepository.existsById(budgetId))
            throw new BudgetNotFoundException("Budget with id: " + budgetId + " not found");
        budgetsRepository.deleteById(budgetId);
    }
}
