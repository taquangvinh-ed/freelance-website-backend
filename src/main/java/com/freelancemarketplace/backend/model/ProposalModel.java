package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.BudgetTypes;
import com.freelancemarketplace.backend.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Proposals")
public class ProposalModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposalId;

    // The name of the proposal
    private String name;

    private String description;

    private String currencyUnit;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    private BigDecimal amount;

    private Integer deliveryDays;


    @Enumerated(EnumType.STRING)
    private BudgetTypes budgetType; // CẦN THÊM ENUM NÀY

    // Mức phí theo giờ được đề xuất (chỉ dùng nếu budgetType là HOURLY)
    private BigDecimal hourlyRate; // CẦN BỔ SUNG

    // Số giờ ước tính (chỉ dùng nếu budgetType là HOURLY)
    private Integer estimatedHours;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false)
    private ProjectModel project;


    @OneToOne(mappedBy = "proposal")
    private ContractModel contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MileStoneModel> mileStones;

    public void validateMilestones() {
        if (mileStones != null && !mileStones.isEmpty()) {
            BigDecimal totalMilestoneAmount = mileStones.stream()
                    .map(MileStoneModel::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalMilestoneAmount.compareTo(amount) != 0) {
                throw new IllegalStateException("Total milestone amount must equal proposal amount");
            }
        }
    }

}
