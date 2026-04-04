package com.freelancemarketplace.backend.domain.model;

import com.freelancemarketplace.backend.domain.enums.MileStoneStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.proposal.domain.model.ProposalModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milestone")
public class MileStoneModel extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mileStoneId;

    private String name;

    private BigDecimal amount;

    private String currencyUnit;

    private Integer dueDate;

    private String description;

    private Boolean isActived;

    private String paymentIntentId;

    private Timestamp completedAt;

    private String fileUrl;
    private String fileName;

    @Enumerated(EnumType.STRING)
    private MileStoneStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="contractId")
    private ContractModel contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalId")
    private ProposalModel proposal;

}
