package com.freelancemarketplace.backend.freelancer.application.service.imp;

import com.freelancemarketplace.backend.freelancer.application.service.FreelancerOnboardingService;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreelancerOnboardingServiceImp implements FreelancerOnboardingService {

    private final FreelancersRepository freelancersRepository;

    @Override
    @Transactional
    public void markOnboardingCompleted(String stripeAccountId) {
        FreelancerModel freelancer = freelancersRepository.findByStripeAccountId(stripeAccountId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer with stripeAccountId: " + stripeAccountId + " not found")
        );

        if (!freelancer.getOnboardingCompleted()) {
            freelancer.setOnboardingCompleted(true);
            freelancersRepository.save(freelancer);
            log.info("Mark onboarding completed successfully");
        }
    }
}
