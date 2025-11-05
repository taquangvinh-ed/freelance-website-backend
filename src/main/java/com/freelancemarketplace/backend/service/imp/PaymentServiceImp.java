package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.dto.PaymentIntentResponse;
import com.freelancemarketplace.backend.enums.PaymentStatus;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.ContractModel;
import com.freelancemarketplace.backend.model.PaymentModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.ContractsRepository;
import com.freelancemarketplace.backend.repository.PaymentsRepository;
import com.freelancemarketplace.backend.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Slf4j
@Service
public class PaymentServiceImp implements PaymentService {

    private PaymentsRepository paymentsRepository;
    private ClientsRepository clientsRepository;
    private ContractsRepository contractsRepository;

    public PaymentServiceImp(PaymentsRepository paymentsRepository, ClientsRepository clientsRepository, ContractsRepository contractsRepository) {
        this.paymentsRepository = paymentsRepository;
        this.clientsRepository = clientsRepository;
        this.contractsRepository = contractsRepository;
    }


    @Override
    public String createStripeCustomer(String email, String name) throws Exception {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
        Customer customer = Customer.create(params);
        return customer.getId();
    }

    @Override
    public PaymentIntentResponse createEscrowPayment(MileStoneDTO mileStoneDTO, Long clientId, ClientDTO clientDTO, Long contractId) throws Exception {

        BigDecimal amount = mileStoneDTO.getAmount();
        BigDecimal postingFee = amount.multiply(BigDecimal.valueOf(0.03));
        BigDecimal total = amount.add(postingFee);

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setAmount(total);
        paymentModel.setStatus(PaymentStatus.PENDING);
        ClientModel client = clientsRepository.findById(clientId).orElseThrow(
                () -> new ResourceNotFoundException("Client with id " + clientId + " not found")
        );
        ContractModel contract = contractsRepository.findById(contractId).orElseThrow(
                () -> new ResourceNotFoundException("Contract with id " + contractId + " not found")
        );

        paymentModel.setClient(client);
        paymentModel.setPaidAt(Timestamp.from(Instant.now()));
        paymentModel.setContract(contract);
        paymentsRepository.save(paymentModel);


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
    public void releasePayment(String paymentIntentId, MileStoneDTO mileStoneDTO, String stripeAccountId) throws Exception {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);

        BigDecimal amount = mileStoneDTO.getAmount();
        BigDecimal freelancerAmount = amount.multiply(BigDecimal.valueOf(0.9));


        intent.capture();

        Transfer.create(TransferCreateParams.builder()
                .setAmount(freelancerAmount.multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("usd")
                .setDestination(stripeAccountId)
                .setSourceTransaction(intent.getId())
                .build());

    }

    @Override
    public void refundPayment(String paymentIntentId) throws Exception {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        intent.cancel();
    }


    @Override
    public String createStripeConnectAccount(String email, String countryCode, Long freelancerId) throws StripeException {
        try {

            AccountCreateParams.Capabilities capabilities = AccountCreateParams.Capabilities.builder()
                    .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
                            .setRequested(true)
                            .build())
                    .setTransfers(AccountCreateParams.Capabilities.Transfers
                            .builder()
                            .setRequested(true)
                            .build())
                    .build();

            AccountCreateParams.Builder builder = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.STANDARD)
                    .setEmail(email)
                    .setCountry(countryCode)
                    .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                    .putMetadata("freelancer_id", freelancerId.toString())
                    .putMetadata("platform", "freelance-marketplace")
                    .setCapabilities(capabilities);

            Account account = Account.create(builder.build());
            return account.getId();
        } catch (StripeException e) {
            log.error("Error create stripe connect account: {}", e.getMessage());
            throw new RuntimeException("Error create stripe connect account: " + e.getMessage());
        }
    }

@Override
public String createFreelancerOnboardingLink(String stripeAccountId, String returnUrl, String refreshUrl) throws Exception {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(stripeAccountId) // ID tài khoản acct_... của Freelancer
                .setRefreshUrl(refreshUrl)   // URL quay lại nếu link hết hạn (404)
                .setReturnUrl(returnUrl)     // URL quay lại trang của bạn sau khi hoàn tất
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);
        return accountLink.getUrl();
    }

}
