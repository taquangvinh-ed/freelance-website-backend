package com.freelancemarketplace.backend.product.api.controller;

import com.freelancemarketplace.backend.product.dto.OrderDTO;
import com.freelancemarketplace.backend.api.response.ApiResponse;
import com.freelancemarketplace.backend.product.application.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/client/{clientId}/product/{productId}")
    ApiResponse<?> purchaseProduct(@PathVariable Long clientId,
                                               @PathVariable Long productId){
        OrderDTO newOrder = orderService.pachaseProduct(clientId, productId);
        return ApiResponse.success(newOrder);
    }

    @PutMapping("/success")
    ApiResponse<?> paymentSuccess(@RequestParam String sessionId){
        orderService.handlePaymentSuccess(sessionId);
        return ApiResponse.success("Pay successfully");
    }


}
