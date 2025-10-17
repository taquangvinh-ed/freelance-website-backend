package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.MileStoneStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    @Enumerated(EnumType.STRING)
    private MileStoneStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="contractId")
    private ContractModel contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalId")
    private ProposalModel proposal;

}
