package com.freelancemarketplace.backend.dto;

import com.freelancemarketplace.backend.enums.ContractStatus;
import com.freelancemarketplace.backend.enums.ContractTypes;
import com.freelancemarketplace.backend.model.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ContractDTO {

    private Long contractId;

    private String types;

    private BigDecimal amount;

    private Timestamp startDate;

    private Timestamp endDate;

    private String status;

    private Long proposalId;

    private Set<Long> paymentIds;

    private Long freelancerId;

    private Long teamId;

    private Long companyId;

    private Long clientId;

    private Long contractProjectId;

    private Set<MileStoneDTO> mileStones;
}
