package com.freelancemarketplace.backend.user.application.service;

import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.dto.RegistrationtDTO;
import com.freelancemarketplace.backend.payment.application.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("freelancerFactory")
@RequiredArgsConstructor
public class FreelancerFactory implements UserFactory<FreelancerModel> {

    private final PaymentService paymentService;

    @Override
    public FreelancerModel createProfile(RegistrationtDTO registrationDTO, UserModel savedUser) throws StripeException {
        FreelancerModel freelancer = new FreelancerModel();
        freelancer.setFreelancerId(savedUser.getUserId());
        freelancer.setFirstName(registrationDTO.getFirstName());
        freelancer.setLastName(registrationDTO.getLastName());
        freelancer.setOnboardingCompleted(false);
        freelancer.setUser(savedUser);

        if (registrationDTO.getSummary() != null) {
            freelancer.getBio().setSummary(registrationDTO.getSummary());
        }
        if (registrationDTO.getFacebookUrl() != null) {
            freelancer.getBio().setFacebookLink(registrationDTO.getFacebookUrl());
        }
        if (registrationDTO.getLinkedlnUrl() != null) {
            freelancer.getBio().setLinkedinLink(registrationDTO.getLinkedlnUrl());
        }
        if (registrationDTO.getGithubUrl() != null) {
            freelancer.getBio().setTwitterLink(registrationDTO.getGithubUrl());
        }

        String stripeConnectAccountId = paymentService.createStripeConnectAccount(
                savedUser.getEmail(), "US", freelancer.getFreelancerId());
        freelancer.setStripeAccountId(stripeConnectAccountId);

        return freelancer;
    }
}
