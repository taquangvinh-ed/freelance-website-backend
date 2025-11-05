package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.ClientDTO;
import com.freelancemarketplace.backend.dto.MileStoneDTO;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.service.FreelancerService;
import com.freelancemarketplace.backend.service.PaymentService;
import com.freelancemarketplace.backend.service.imp.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<String> handle(@RequestBody String payload,
                                         @RequestHeader("Stripe-Signature") String signature) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, signature, stripeWebhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        if ("account.updated".equals(event.getType())) {
            Account account = (Account) event.getData().getObject();

            boolean ready = account.getChargesEnabled() && account.getPayoutsEnabled();
            if (ready) {
                String acctId = account.getId();
                freelancerService.markOnboardingCompleted(acctId);
            }
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/create-onboarding-link")
    public ResponseEntity<String> createOnboardingLink(@AuthenticationPrincipal AppUser appUser) throws Exception {
        Long freelancerId = appUser.getId();
        FreelancerModel freelancer = freelancerService.findById(freelancerId);

        if (freelancer.getOnboardingCompleted()) {
            return ResponseEntity.badRequest().body("Already completed");
        }

        String link = paymentService.createFreelancerOnboardingLink(
                freelancer.getStripeAccountId(),
                "http://localhost:3000/onboarding-result?onboarding=success",
                "https://yourapp.com/dashboard?onboarding=refresh"
        );

        return ResponseEntity.ok(link);
    }


}
