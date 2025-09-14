package com.freelancemarketplace.backend.model;

import com.freelancemarketplace.backend.enums.PaymentStatus;
import com.freelancemarketplace.backend.enums.PaymentTypes;
import com.freelancemarketplace.backend.enums.RecuiterTypes;
import com.freelancemarketplace.backend.enums.TailentTypes;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
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

    public PaymentsModel() {
    }

    public Long getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(Long payment_id) {
        this.payment_id = payment_id;
    }

    public RecuiterTypes getRecuiter() {
        return recuiter;
    }

    public void setRecuiter(RecuiterTypes recuiter) {
        this.recuiter = recuiter;
    }

    public TailentTypes getTailent() {
        return tailent;
    }

    public void setTailent(TailentTypes tailent) {
        this.tailent = tailent;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public PaymentTypes getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(PaymentTypes payment_type) {
        this.payment_type = payment_type;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Timestamp getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(Timestamp paid_at) {
        this.paid_at = paid_at;
    }

    public FreelancersModel getFreelancerPayments() {
        return freelancerPayments;
    }

    public void setFreelancerPayments(FreelancersModel freelancerPayments) {
        this.freelancerPayments = freelancerPayments;
    }

    public ContractsModel getContractPayments() {
        return contractPayments;
    }

    public void setContractPayments(ContractsModel contractPayments) {
        this.contractPayments = contractPayments;
    }

    public ClientsModel getClientPayments() {
        return clientPayments;
    }

    public void setClientPayments(ClientsModel clientPayments) {
        this.clientPayments = clientPayments;
    }

    public CompaniesModel getCompanyPayments() {
        return companyPayments;
    }

    public void setCompanyPayments(CompaniesModel companyPayments) {
        this.companyPayments = companyPayments;
    }

    public TeamsModel getTeamPayments() {
        return teamPayments;
    }

    public void setTeamPayments(TeamsModel teamPayments) {
        this.teamPayments = teamPayments;
    }
}
