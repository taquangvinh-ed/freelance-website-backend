package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.OrderDTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/client/{clientId}/product/{productId}")
    ResponseEntity<ResponseDTO>purchaseProduct(@PathVariable Long clientId,
                                               @PathVariable Long productId){
        OrderDTO newOrder = orderService.pachaseProduct(clientId, productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS, newOrder));
    }

    @PutMapping("/success")
    ResponseEntity<ResponseDTO>paymentSuccess(@RequestParam String sessionId){
        orderService.handlePaymentSuccess(sessionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        "Pay successfully"
                ));
    }


}
