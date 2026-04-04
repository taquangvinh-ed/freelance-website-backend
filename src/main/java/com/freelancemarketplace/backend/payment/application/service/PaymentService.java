package com.freelancemarketplace.backend.payment.application.service;

import com.freelancemarketplace.backend.client.dto.ClientDTO;
import com.freelancemarketplace.backend.contract.dto.MileStoneDTO;
import com.freelancemarketplace.backend.email.dto.PaymentIntentResponse;
import com.freelancemarketplace.backend.contract.domain.model.ContractModel;
import com.freelancemarketplace.backend.payment.domain.model.PaymentModel;
import com.freelancemarketplace.backend.contract.domain.model.WeeklyReportModel;
import com.stripe.exception.StripeException;

public interface PaymentService {

    String createStripeCustomer(String email, String name) throws Exception;

    public PaymentIntentResponse createEscrowPayment(MileStoneDTO mileStoneDTO, Long clientId, ClientDTO clientDTO, Long contractId) throws Exception;

    void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String freelancerstripeCustomerId) throws Exception;

    void refundPayment(String paymentIntentId) throws Exception;

    String createStripeConnectAccount(String email, String countryCode, Long freelancerId) throws StripeException;

    String createFreelancerOnboardingLink(String stripeAccountId, String returnUrl, String refreshUrl) throws Exception;

    PaymentModel createHourlyPaymentFromWeeklyReport(ContractModel contract, WeeklyReportModel weeklyReport);
}
