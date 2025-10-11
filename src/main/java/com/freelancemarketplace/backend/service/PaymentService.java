package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;

public interface PaymentService {

    public String createEscrowPayment(MileStoneDTO mileStoneDTO, ClientDTO clientDTO) throws Exception;

    void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String freelancerstripeCustomerId) throws Exception;

    void refundPayment(String paymentIntentId) throws Exception;
}
