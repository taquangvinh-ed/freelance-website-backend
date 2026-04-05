package com.freelancemarketplace.backend.domain.model;

import com.freelancemarketplace.backend.domain.enums.BudgetTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;

@Entity(name = "LegacyBudgetModel")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Budgets")
public class BudgetModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @Enumerated(EnumType.STRING)
    private BudgetTypes type;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private BigDecimal fixedValue;

    private String currencyUnit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;

}
