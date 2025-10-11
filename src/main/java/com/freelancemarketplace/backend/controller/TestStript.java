package com.freelancemarketplace.backend.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountLinkCreateParams;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class TestStript {
    @GetMapping("/onboarding/refresh")
    public RedirectView refreshOnboarding(@RequestParam(required = false) String account_id) throws StripeException {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(account_id)
                .setRefreshUrl("http://localhost:8080/onboarding/refresh")
                .setReturnUrl("http://localhost:8080/onboarding/complete")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);

        // 2. Chuyển hướng người dùng trở lại URL của Stripe
        // RedirectView là class của Spring để thực hiện chuyển hướng
        return new RedirectView(accountLink.getUrl());    }

    @GetMapping("/onboarding/complete")
    public String complete(@RequestParam String account_id ){
        return "Successfully" + account_id;
    }
}
