package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.PaymentStatus;
import com.freelancemarketplace.backend.enums.PaymentTypes;
import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PaymentsModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payment_id;

    @Enumerated(EnumType.STRING)
    private RecuiterTypes recuiter;

    @Enumerated(EnumType.STRING)
    private TailentTypes tailent;

    private String amount;

    @Enumerated(EnumType.STRING)
    private PaymentTypes payment_type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transaction_id;

    private Timestamp paid_at;

    //The payments that belong to a freelancer
    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private FreelancersModel freelancerPayments;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private ContractsModel contractPayments;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientsModel clientPayments;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompaniesModel companyPayments;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamsModel teamPayments;

}
