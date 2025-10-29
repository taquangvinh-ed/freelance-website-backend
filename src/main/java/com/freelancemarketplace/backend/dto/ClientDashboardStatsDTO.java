package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ClientDashboardStatsDTO {
    int totalProjects;
    int activeProjects;
    int pendingPayments;
    BigDecimal totalSpent;
}
