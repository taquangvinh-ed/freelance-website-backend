package com.freelancemarketplace.backend.infrastructure.mapper;

import com.freelancemarketplace.backend.project.dto.BudgetDTO;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BudgetMapper {
    BudgetModel toEntity(BudgetDTO budgetDTO);

    @Mapping(target="projectId", source = "project.projectId")
    BudgetDTO toDto(BudgetModel budgetModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BudgetModel partialUpdate(BudgetDTO budgetDTO, @MappingTarget BudgetModel budgetModel);
}