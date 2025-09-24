package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.OrderDTO;
import com.freelancemarketplace.backend.enums.OrderStatus;
import com.freelancemarketplace.backend.enums.PaymentStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.OrderMapper;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.OrderModel;
import com.freelancemarketplace.backend.model.PaymentModel;
import com.freelancemarketplace.backend.model.ProductModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.OrderRepository;
import com.freelancemarketplace.backend.repository.PaymentsRepository;
import com.freelancemarketplace.backend.repository.ProductsRepository;
import com.freelancemarketplace.backend.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class OrderServiceImp implements OrderService {

    private final ProductsRepository productsRepository;
    private final ClientsRepository clientsRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentsRepository paymentsRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeKey;

    public OrderServiceImp(ProductsRepository productsRepository,
                           ClientsRepository clientsRepository,
                           OrderRepository orderRepository,
                           OrderMapper orderMapper,
                           PaymentsRepository paymentsRepository) {
        this.productsRepository = productsRepository;
        this.clientsRepository = clientsRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.paymentsRepository = paymentsRepository;
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
        newOrder.setStatus(OrderStatus.PENDING);
        OrderModel savedOrder = orderRepository.save(newOrder);


        product.setViews(product.getViews()+1);
        product.setTimeOfDownload(System.currentTimeMillis());
        productsRepository.save(product);


        PaymentModel newPayment = new PaymentModel();
        newPayment.setFreelancer(product.getFreelancer());
        newPayment.setClient(client);
        newPayment.setAmount(product.getPrice());
        newPayment.setStatus(PaymentStatus.PENDING);

        //Create Stripe session
        Stripe.apiKey = stripeKey;
        String redirectUrl = "";
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/api/orders/success?sessionId={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/api/orders/cancel")
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(product.getName())
                                                                    .build()
                                                    )
                                                    .setUnitAmount(product.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .build()
                                    ).build()

                    )
                    .build();
            Session session = Session.create(params);
            newPayment.setTransactionId(session.getId());
            redirectUrl =  session.getUrl();

            newPayment.setOrder(savedOrder);
            paymentsRepository.save(newPayment);

        }catch (StripeException e){
            throw new RuntimeException("Stripe error: " +e);
        }

        OrderDTO orderDTO = orderMapper.toDto(savedOrder);
        orderDTO.setRedirectUrl(redirectUrl);
        return orderDTO;
    }

    @Transactional
    @Override
    public void handlePaymentSuccess(String sessionId) {
        PaymentModel payment = paymentsRepository.findByTransactionId(sessionId);
        if (payment == null)
            throw new ResourceNotFoundException("payment with id: " + sessionId + " not found");
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(Timestamp.from(Instant.now()));
        payment.setPaidAt(new Timestamp(System.currentTimeMillis()));
        payment.getOrder().setStatus(OrderStatus.PAID);
        paymentsRepository.save(payment);
    }
}
