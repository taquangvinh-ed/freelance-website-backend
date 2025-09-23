package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.OrderDTO;

public interface OrderService {

    OrderDTO pachaseProduct(Long clientId, Long productId);

}
