package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.model.ProjectModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDTO {
    private Long budgetId;

    private BudgetTypes type;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private BigDecimal fixedValue;

    private String currencyUnit;

    private Long projectId;
}
