    package com.freelancemarketplace.backend.service.imp;

    import com.freelancemarketplace.backend.dto.ClientDTO;
    import com.freelancemarketplace.backend.dto.MileStoneDTO;
    import com.freelancemarketplace.backend.dto.PaymentIntentResponse;
    import com.freelancemarketplace.backend.service.PaymentService;
    import com.stripe.model.PaymentIntent;
    import com.stripe.model.Transfer;
    import com.stripe.param.PaymentIntentCreateParams;
    import com.stripe.param.TransferCreateParams;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;

    @Service
    public class PaymentServiceImp implements PaymentService {


        public PaymentServiceImp() {
        }

        @Override
        public PaymentIntentResponse createEscrowPayment(MileStoneDTO mileStoneDTO, ClientDTO clientDTO) throws Exception {

            BigDecimal amount = mileStoneDTO.getAmount();
            BigDecimal postingFee = amount.multiply(BigDecimal.valueOf(0.03));
            BigDecimal total = amount.add(postingFee);


            PaymentIntent intent = PaymentIntent.create(
                    PaymentIntentCreateParams.builder()
                            .setAmount(total.multiply(BigDecimal.valueOf(100)).longValue()) // In cents
                            .setCurrency("usd")
                            .setCustomer(clientDTO.getStripeCustomerId())
                            .addPaymentMethodType("card")
                            .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL) // Hold funds
                            .setDescription("Escrow for milestone: " + mileStoneDTO.getDescription())
                            .build()
            );
            return new PaymentIntentResponse(intent.getId(), intent.getClientSecret());
        }

        @Override
        public void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String freelancerstripeCustomerId) throws Exception {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

            BigDecimal amount = mileStoneDTO.getAmount();
            BigDecimal freelancerAmount = amount.multiply(BigDecimal.valueOf(0.9));


            intent.capture();

            Transfer.create(TransferCreateParams.builder()
                    .setAmount(freelancerAmount.multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency("vnd")
                    .setDestination(freelancerstripeCustomerId)
                    .setSourceTransaction(intent.getId())
                    .build());

        }

        @Override
        public void refundPayment(String paymentIntentId) throws Exception {
            PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
            intent.cancel();
        }
    }
