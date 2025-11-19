package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.dto.SkillDTO2;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkillSerivice {

    SkillDTO createSkill(SkillDTO skillDTO);

    SkillDTO2 createSkillOrAddToCategories(SkillDTO2 skillDTO2);

    SkillDTO updateSkill(Long skillId, SkillDTO2 skillDTO);

    void deleteSkill(Long skillId);

    SkillDTO getSkillById(Long skillId);

    List<SkillDTO> getAllSkill();

    List<SkillDTO> getAllSkillByCategory(Long CategoryId);


    List<SkillDTO> autoCompleteSearchSkill(String keyword, Pageable pageable);
}
