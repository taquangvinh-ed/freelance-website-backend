package com.freelancemarketplace.backend.freelancer.application.service;

import com.freelancemarketplace.backend.freelancer.dto.ExperienceDTO;

import java.util.List;

public interface ExperienceService {

    ExperienceDTO create(Long freelancerId, ExperienceDTO experienceDTO);

    ExperienceDTO update(Long experienceId, ExperienceDTO experienceDTO);

    void delete(Long experienceId);

    List<ExperienceDTO> getAllExperienceByFreelancer(Long freelancerId);

}
