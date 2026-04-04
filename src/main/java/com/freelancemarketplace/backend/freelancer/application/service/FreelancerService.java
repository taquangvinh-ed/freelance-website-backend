package com.freelancemarketplace.backend.freelancer.application.service;

import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerInfoDTO;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FreelancerService {

    FreelancerDTO createFreelancer(FreelancerDTO freelancerDTO);

    FreelancerDTO updateFreelancer(Long freelancerId, FreelancerDTO freelancerDTO);

    void deleteFreelancer(Long freelancerId);

    List<FreelancerDTO>getAllFreelancer();

    FreelancerDTO getFreelancerById(Long freelancerId);

    FreelancerInfoDTO getInfo(Long freelancerId);

    FreelancerModel findById(Long freelancerId);

    FreelancerDTO assignSkillToFreelancer(Long freelancerId, Long skillId);

    FreelancerDTO removeSkillFromFreelancer(Long freelancerId, Long skillId);

    void markOnboardingCompleted(String stripeAccountId);

    String uploadAvatar(Long freelancerId, MultipartFile file) throws IOException;
}
