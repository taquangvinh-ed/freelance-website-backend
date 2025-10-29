package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.PaymentIntentResponse;

public interface PaymentService {

    String createStripeCustomer(String email, String name) throws Exception;

    public PaymentIntentResponse createEscrowPayment(MileStoneDTO mileStoneDTO, Long clientId, ClientDTO clientDTO, Long contractId) throws Exception;

    void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String freelancerstripeCustomerId) throws Exception;

    void refundPayment(String paymentIntentId) throws Exception;
}
