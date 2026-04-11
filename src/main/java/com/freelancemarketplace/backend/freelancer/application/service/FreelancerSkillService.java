package com.freelancemarketplace.backend.freelancer.application.service;

import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;

public interface FreelancerSkillService {

    FreelancerDTO assignSkillToFreelancer(Long freelancerId, Long skillId);

    FreelancerDTO removeSkillFromFreelancer(Long freelancerId, Long skillId);
}
