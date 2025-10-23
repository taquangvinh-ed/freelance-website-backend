package com.freelancemarketplace.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ContractResponseDTO {
    private Long contractId;

    private String types;

    private double amount;

    private Timestamp startDate;

    private Timestamp endDate;

    private String status;

    private Long proposalId;

    private String freelancerName;

    private String clientName;

    private List<MileStoneDTO> mileStones;
}
