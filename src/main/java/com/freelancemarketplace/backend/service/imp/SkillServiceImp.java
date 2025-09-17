package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.SkillAlreadyExisted;
import com.freelancemarketplace.backend.mapper.SkillMapper;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.service.SkillSerivice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImp implements SkillSerivice {

    private SkillsRepository skillsRepository;
    private SkillMapper skillMapper;

    public SkillServiceImp(SkillsRepository skillsRepository, SkillMapper skillMapper) {
        this.skillsRepository = skillsRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        if(skillsRepository.existsByNameIgnoreCase(skillDTO.getName()))
            throw new SkillAlreadyExisted("Skill has already exitsed");
        SkillModel newSkill = skillMapper.toEntity(skillDTO);
        SkillModel savedSkill = skillsRepository.save(newSkill);
        return skillMapper.toDTO(savedSkill);
    }

    @Override
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
    public void deleteSkill(Long skillId) {

    }

    @Override
    public SkillDTO getSkillById(Long skillId) {
        return null;
    }

    @Override
    public List<SkillDTO> getAllSkill() {
        return List.of();
    }

    @Override
    public List<SkillDTO> getAllSkillByName(String skillName) {
        return List.of();
    }

    @Override
    public void assignSkillToFreelancer(Long freelancerId, Long skillId) {

    }

    @Override
    public void removeSkillFromFreelancer(Long freelancerId, Long skillId) {

    }
}
