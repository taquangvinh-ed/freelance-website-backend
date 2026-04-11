package com.freelancemarketplace.backend.freelancer.application.service;

import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;

import java.util.List;

public interface FreelancerCrudService {

    FreelancerDTO createFreelancer(FreelancerDTO freelancerDTO);

    FreelancerDTO updateFreelancer(Long freelancerId, FreelancerDTO freelancerDTO);

    void deleteFreelancer(Long freelancerId);

    List<FreelancerDTO> getAllFreelancer();

    FreelancerDTO getFreelancerById(Long freelancerId);
}
