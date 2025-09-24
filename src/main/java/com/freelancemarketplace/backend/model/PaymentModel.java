package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.PaymentStatus;
import com.freelancemarketplace.backend.enums.PaymentTypes;
import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
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
@Table(name = "Payments")
public class PaymentModel extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

//    @Enumerated(EnumType.STRING)
//    private RecuiterTypes recruiter;

//    @Enumerated(EnumType.STRING)
//    private TailentTypes talent;


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
