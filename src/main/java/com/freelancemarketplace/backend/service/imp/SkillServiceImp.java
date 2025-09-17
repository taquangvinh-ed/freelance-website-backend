package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.SkillAlreadyExisted;
import com.freelancemarketplace.backend.mapper.SkillMapper;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.service.SkillSerivice;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImp implements SkillSerivice {

    private SkillsRepository skillsRepository;
    private SkillMapper skillMapper;
    private FreelancersRepository freelancersRepository;

    public SkillServiceImp(SkillsRepository skillsRepository,
                           SkillMapper skillMapper,
                           FreelancersRepository freelancersRepository) {
        this.skillsRepository = skillsRepository;
        this.skillMapper = skillMapper;
        this.freelancersRepository = freelancersRepository;
    }

    @Override
    @Transactional
    public SkillDTO createSkill(SkillDTO skillDTO) {
        if(skillsRepository.existsByNameIgnoreCase(skillDTO.getName()))
            throw new SkillAlreadyExisted("Skill has already exitsed");
        SkillModel newSkill = skillMapper.toEntity(skillDTO);
        SkillModel savedSkill = skillsRepository.save(newSkill);
        return skillMapper.toDTO(savedSkill);
    }

    @Override
    @Transactional
    public SkillDTO updateSkill(Long skillId, SkillDTO skillDTO) {
        SkillModel skillModel = skillsRepository.findById(skillId).orElseThrow(
                ()-> new ResourceNotFoundException("Skill with id: " + skillId+" not found")
        );

        if(skillDTO.getName() != null)
            skillModel.setName(skillDTO.getName());
        if(skillDTO.getDescription()!=null)
            skillModel.setDescription(skillDTO.getDescription());

        SkillModel savedSkill = skillsRepository.save(skillModel);
        return skillMapper.toDTO(savedSkill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long skillId) {
        if(!skillsRepository.existsById(skillId))
            throw new ResourceNotFoundException("Skill with id: " + skillId + " not found");
        skillsRepository.deleteById(skillId);
    }

    @Override
    public SkillDTO getSkillById(Long skillId) {
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                ()->new ResourceNotFoundException("Skill with id: " + skillId + " not found"));
        return skillMapper.toDTO(skill);
    }

    @Override
    public List<SkillDTO> getAllSkill() {
        List<SkillModel> skills = skillsRepository.findAll();
        return skillMapper.toDTOs(skills);
    }

    @Override
    public List<SkillDTO> getAllSkillByName(String skillName) {
        return List.of();
    }

    @Override
    public void assignSkillToFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()->new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                ()->new ResourceNotFoundException("Skill with id: " + skillId + " not found"));

        freelancer.getSkills().add(skill);

        freelancersRepository.save(freelancer);
    }

    @Override
    public void removeSkillFromFreelancer(Long freelancerId, Long skillId) {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId).orElseThrow(
                ()->new ResourceNotFoundException("Freelancer with id: " + freelancerId + " not found")
        );

        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                ()->new ResourceNotFoundException("Skill with id: " + skillId + " not found"));

        freelancer.getSkills().remove(skill);

        freelancersRepository.save(freelancer);

    }
}
