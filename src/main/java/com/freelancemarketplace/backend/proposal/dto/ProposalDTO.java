package com.freelancemarketplace.backend.proposal.dto;

import com.freelancemarketplace.backend.project.domain.enums.BudgetTypes;
import com.freelancemarketplace.backend.proposal.domain.enums.ProposalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;

@Getter
@Setter
@NoArgsConstructor
public class ProposalDTO {

    private Long proposalId;

    private String name;

    private String description;

    private String currencyUnit;

    private String status;

    private BigDecimal amount;

    private String budgetType; // CẦN THÊM ENUM NÀY

    // Mức phí theo giờ được đề xuất (chỉ dùng nếu budgetType là HOURLY)
    private BigDecimal hourlyRate; // CẦN BỔ SUNG

    // Số giờ ước tính (chỉ dùng nếu budgetType là HOURLY)
    private Integer estimatedHours;

    private Integer deliveryDays;

    @NotNull(message = "Project id must not be null")
    private Long projectId;

    private Long freelancerId;

    private Long teamId;

    private Set<MileStoneDTO> mileStones;
}
