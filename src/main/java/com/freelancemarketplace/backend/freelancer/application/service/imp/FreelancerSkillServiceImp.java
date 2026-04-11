package com.freelancemarketplace.backend.freelancer.application.service.imp;

import com.freelancemarketplace.backend.freelancer.application.service.FreelancerSkillService;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.freelancer.infrastructure.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.skill.infrastructure.repository.SkillsRepository;
import com.freelancemarketplace.backend.recommendation.application.service.EmbeddingService;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerSkillServiceImp implements FreelancerSkillService {

    private final FreelancersRepository freelancersRepository;
    private final SkillsRepository skillsRepository;
    private final FreelancerMapper freelancerMapper;
    private final EmbeddingService embeddingService;

    @Override
    @Transactional
    public FreelancerDTO assignSkillToFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId)
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill not found with id: " + skillId));

        freelancer.getSkills().add(skill);
        freelancer.setSkillVector(embeddingService.generateSkillVector(freelancer));

        FreelancerModel savedFreelancer = freelancersRepository.save(freelancer);
        return freelancerMapper.toDTO(savedFreelancer);
    }

    @Override
    @Transactional
    public FreelancerDTO removeSkillFromFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                () -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId)
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill not found with id: " + skillId));

        freelancer.getSkills().remove(skill);
        freelancer.setSkillVector(embeddingService.generateSkillVector(freelancer));

        FreelancerModel savedFreelancer = freelancersRepository.save(freelancer);
        return freelancerMapper.toDTO(savedFreelancer);
    }
}
