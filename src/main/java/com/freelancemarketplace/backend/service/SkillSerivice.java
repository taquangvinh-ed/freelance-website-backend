package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.SkillDTO;

import java.util.List;

public interface SkillSerivice {

    SkillDTO createSkill(SkillDTO skillDTO);

    SkillDTO updateSkill(Long skillId, SkillDTO skillDTO);

    void deleteSkill(Long skillId);

    SkillDTO getSkillById(Long skillId);

    List<SkillDTO> getAllSkill();

    List<SkillDTO> getAllSkillByCategory(Long CategoryId);

    public void assignSkillToFreelancer(Long freelancerId, Long skillId);

    public void removeSkillFromFreelancer(Long freelancerId, Long skillId);



}
