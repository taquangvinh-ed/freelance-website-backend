package com.freelancemarketplace.backend.product.application.service;

import com.freelancemarketplace.backend.product.dto.OrderDTO;
import jakarta.transaction.Transactional;

public interface OrderService {

    OrderDTO pachaseProduct(Long clientId, Long productId);

    @Transactional
    void handlePaymentSuccess(String sessionId);
}
