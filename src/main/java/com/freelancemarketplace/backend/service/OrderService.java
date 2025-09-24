package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    OrderDTO pachaseProduct(Long clientId, Long productId);

    @Transactional
    void handlePaymentSuccess(String sessionId);
}
