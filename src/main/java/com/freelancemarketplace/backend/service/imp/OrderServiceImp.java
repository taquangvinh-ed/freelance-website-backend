package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.OrderDTO;
import com.freelancemarketplace.backend.enums.OrderStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.OrderMapper;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.OrderModel;
import com.freelancemarketplace.backend.model.ProductModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.OrderRepository;
import com.freelancemarketplace.backend.repository.ProductsRepository;
import com.freelancemarketplace.backend.service.OrderService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class OrderServiceImp implements OrderService {

    private final ProductsRepository productsRepository;
    private final ClientsRepository clientsRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImp(ProductsRepository productsRepository, ClientsRepository clientsRepository, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.productsRepository = productsRepository;
        this.clientsRepository = clientsRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO pachaseProduct(Long clientId, Long productId) {

        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Client with id: " + clientId + " not found")
        );

        ProductModel product = productsRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product with id: " + productId + " not found")
        );

        OrderModel newOrder = new OrderModel();

        newOrder.setClient(client);
        newOrder.setProduct(product);
        newOrder.setPrice(product.getPrice());
        newOrder.setPurchaseDate(Timestamp.from(Instant.now()));
        newOrder.setTimeOfDownload(System.currentTimeMillis());
        newOrder.setStatus(OrderStatus.PENDING);

        product.setViews(product.getViews()+1);
        product.setTimeOfDownload(System.currentTimeMillis());

        productsRepository.save(product);

        OrderModel savedOrder = orderRepository.save(newOrder);

        return orderMapper.toDto(savedOrder);
    }
}
