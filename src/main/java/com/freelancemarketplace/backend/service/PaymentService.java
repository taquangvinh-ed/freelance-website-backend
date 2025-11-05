package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.PaymentIntentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    String createStripeCustomer(String email, String name) throws Exception;

    public PaymentIntentResponse createEscrowPayment(MileStoneDTO mileStoneDTO, Long clientId, ClientDTO clientDTO, Long contractId) throws Exception;

    void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String freelancerstripeCustomerId) throws Exception;

    void refundPayment(String paymentIntentId) throws Exception;

    String createStripeConnectAccount(String email, String countryCode, Long freelancerId) throws StripeException;

    String createFreelancerOnboardingLink(String stripeAccountId, String returnUrl, String refreshUrl) throws Exception;
}
