package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.model.FreelancerModel;

import java.util.List;

public interface FreelancerService {

    FreelancerDTO createFreelancer(FreelancerDTO freelancerDTO);

    FreelancerDTO updateFreelancer(Long freelancerId, FreelancerDTO freelancerDTO);

    void deleteFreelancer(Long freelancerId);

    List<FreelancerDTO>getAllFreelancer();

    FreelancerDTO getFreelancerById(Long freelancerId);

    FreelancerModel findById(Long freelancerId);

    FreelancerDTO assignSkillToFreelancer(Long freelancerId, Long skillId);

    FreelancerDTO removeSkillFromFreelancer(Long freelancerId, Long skillId);

    void markOnboardingCompleted(String stripeAccountId);
}
