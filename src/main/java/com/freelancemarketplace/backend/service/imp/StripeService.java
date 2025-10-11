package com.freelancemarketplace.backend.service.imp;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeService {



    public StripeService(@Value("${STRIPE_SECRET_KEY}") String stripeApiKey) {
    }

    public static class OnboardingResponse{
        public String accountId;
        public String onboardingUrl;

        public OnboardingResponse(String accountId, String onboardingUrl) {
            this.accountId = accountId;
            this.onboardingUrl = onboardingUrl;
        }

    }

    public OnboardingResponse createFreelancerAccount(String email, String countryCode) throws StripeException {
        AccountCreateParams accountParams = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.STANDARD)
                .setCountry(countryCode)
                .setEmail(email)
                .setCapabilities(
                        AccountCreateParams.Capabilities.builder()
                                .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                                .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                                .build()
                )
                .build();
        Account account = Account.create(accountParams);

        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(account.getId())
                .setReturnUrl("http://localhost:8080/onboarding/complete?account_id=" + account.getId())
                .setRefreshUrl("http://localhost:8080/onboarding/refresh?account_id=" + account.getId())
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(linkParams);

        return new OnboardingResponse(account.getId(), accountLink.getUrl());
    }


    public String createTestClientCustomer(String email, String name) throws StripeException {
        // Kiểm tra đầu vào
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        try {
            // 1. Tạo Customer
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .build();
            Customer customer = Customer.create(customerParams);

            // 2. TẠO PAYMENT METHOD TỪ TOKEN THỬ NGHIỆM (ĐÃ SỬA LỖI setToken)
            // Chúng ta tạo Map<String, Object> để truyền token, vì Builder không hỗ trợ trực tiếp.
            Map<String, Object> cardParams = new HashMap<>();
            cardParams.put("token", "tok_visa"); // Sử dụng token thử nghiệm

            Map<String, Object> paymentMethodParams = new HashMap<>();
            paymentMethodParams.put("type", "card");
            paymentMethodParams.put("card", cardParams); // Gán token vào trường "card"

            // 3. Tạo PaymentMethod
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);

            // 4. Gắn PaymentMethod vào Customer (Vẫn cần gọi attach rõ ràng)
            PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
                    .setCustomer(customer.getId())
                    .build();
            paymentMethod = paymentMethod.attach(attachParams); // Cập nhật paymentMethod instance

            // 5. Đặt PaymentMethod làm mặc định
            CustomerUpdateParams updateParams = CustomerUpdateParams.builder()
                    .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
                            .setDefaultPaymentMethod(paymentMethod.getId())
                            .build())
                    .build();
            customer.update(updateParams);

            // Trả về Customer ID và Payment Method ID
            return customer.getId();
        } catch (StripeException e) {
            // Thay log.error bằng logger thực tế của bạn
            // log.error("Failed to create customer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create customer: " + e.getMessage(), e);
        }
    }
}
