package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.SkillAlreadyExisted;
import com.freelancemarketplace.backend.mapper.SkillMapper;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.repository.CategoriesRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.service.SkillSerivice;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SkillServiceImp implements SkillSerivice {

    private SkillsRepository skillsRepository;
    private SkillMapper skillMapper;
    private FreelancersRepository freelancersRepository;
    private CategoriesRepository categoriesRepository;


    @Override
    @Transactional
    public SkillDTO createSkill(SkillDTO skillDTO) {
        if(skillsRepository.existsByNameIgnoreCase(skillDTO.getName()))
            throw new SkillAlreadyExisted("Skill has already exitsed");
        SkillModel newSkill = skillMapper.toEntity(skillDTO);
        CategoryModel category = categoriesRepository.findById(skillDTO.getCategoryId()).orElseThrow(
                ()-> new ResourceNotFoundException("Category with id: " + skillDTO.getCategoryId()+" not found")
        );
        newSkill.getCategories().add(category);
        category.getSkills().add(newSkill);
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
    public List<SkillDTO> getAllSkillByCategory(Long categoryId) {
        CategoryModel category = categoriesRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category with id: " + categoryId+" not found")
        );
        List<SkillModel> allskillsByCategory = skillsRepository.findAllByCategoriesContains(category);
        return skillMapper.toDTOs(allskillsByCategory);
    }




}
