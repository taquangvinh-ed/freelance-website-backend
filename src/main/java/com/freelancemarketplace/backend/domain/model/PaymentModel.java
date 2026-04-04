package com.freelancemarketplace.backend.domain.model;

import com.freelancemarketplace.backend.domain.enums.PaymentStatus;
import com.freelancemarketplace.backend.domain.enums.PaymentTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.audit.domain.model.BaseEntity;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.domain.model.CompanyModel;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.product.domain.model.OrderModel;
import com.freelancemarketplace.backend.team.domain.model.TeamModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Payments")
public class PaymentModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentTypes type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;

    private Timestamp paidAt;

    //The payments that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancerId")
    private FreelancerModel freelancer;

    @ManyToOne
    @JoinColumn(name = "contractId")
    private ContractModel contract;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private CompanyModel company;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamModel team;

    @OneToOne
    @JoinColumn(name = "orderId")
    private OrderModel order;

}
