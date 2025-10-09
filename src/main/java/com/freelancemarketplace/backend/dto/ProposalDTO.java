package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.enums.ProposalStatus;
import com.freelancemarketplace.backend.model.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

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

    private Long teamId;

    private Set<MileStoneDTO> mileStones;
}
