package com.freelancemarketplace.backend.payment.application.service;

import com.stripe.exception.StripeException;

public interface StripeService {

    record OnboardingResponse(String accountId, String onboardingUrl) {}

    OnboardingResponse createFreelancerAccount(String email, String countryCode) throws StripeException;

    String createTestClientCustomer(String email, String name) throws StripeException;
}
