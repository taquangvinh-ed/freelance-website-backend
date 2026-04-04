package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.service.FreelancerService;
import com.freelancemarketplace.backend.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Account;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/stripe")
public class StripeController {

    private final String stripeWebhookSecret;
    private final FreelancerService freelancerService;
    private final PaymentService paymentService;

    public StripeController(@Value("${STRIPE_WEBHOOK_SECRET}") String stripeWebhookSecret,
                            FreelancerService freelancerService,
                            PaymentService paymentService) {
        this.stripeWebhookSecret = stripeWebhookSecret;
        this.freelancerService = freelancerService;
        this.paymentService = paymentService;
    }


    @PostMapping("/webhook")
    public ApiResponse<?> handle(@RequestBody String payload,
                                         @RequestHeader("Stripe-Signature") String signature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            return ApiResponse.success(e.getMessage());
        }

        if ("account.updated".equals(event.getType())) {
            Account account = (Account) event.getData().getObject();

            boolean ready = account.getChargesEnabled() && account.getPayoutsEnabled();
            if (ready) {
                String acctId = account.getId();
                freelancerService.markOnboardingCompleted(acctId);
            }
        }
        return ApiResponse.success("OK");
    }

    @PostMapping("/create-onboarding-link")
    public ApiResponse<?> createOnboardingLink(@AuthenticationPrincipal AppUser appUser) throws Exception {
        Long freelancerId = appUser.getId();
        FreelancerModel freelancer = freelancerService.findById(freelancerId);

        if (freelancer.getOnboardingCompleted()) {
            return ApiResponse.success("Already completed");
        }

        String link = paymentService.createFreelancerOnboardingLink(
                freelancer.getStripeAccountId(),
                "http://localhost:3000/onboarding-result?onboarding=success",
                "https://yourapp.com/dashboard?onboarding=refresh"
        );

        return ApiResponse.success(link);
    }


}
