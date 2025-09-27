package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImp implements PaymentService {

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeApiKey;

    @Override
    public String createEscrowPayment(MileStoneDTO mileStoneDTO, ClientDTO clientDTO) throws Exception {
        Stripe.apiKey = stripeApiKey;

        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(mileStoneDTO.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // In cents
                        .setCurrency("usd")
                        .setCustomer(clientDTO.getStripeCustomerId())
                        .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL) // Hold funds
                        .setDescription("Escrow for milestone: " + mileStoneDTO.getDescription())
                        .build()
        );
        return intent.getId();
    }

    public void releasePayment(String paymentIntentId, BigDecimal amount) throws Exception {
        Stripe.apiKey = stripeApiKey;
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        intent.capture();
    }

    public void refundPayment(String paymentIntentId) throws Exception {
        Stripe.apiKey = stripeApiKey;
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        intent.cancel();
    }
}
