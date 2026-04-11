package com.freelancemarketplace.backend.freelancer.application.service;

public interface FreelancerOnboardingService {

    void markOnboardingCompleted(String stripeAccountId);
}
