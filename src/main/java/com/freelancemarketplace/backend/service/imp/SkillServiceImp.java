package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.exception.SkillAlreadyExisted;
import com.freelancemarketplace.backend.mapper.SkillMapper;
import com.freelancemarketplace.backend.model.CategoryModel;
import com.freelancemarketplace.backend.model.SkillModel;
import com.freelancemarketplace.backend.repository.CategoriesRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.service.SkillSerivice;
import com.freelancemarketplace.backend.specification.SkillSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        CategoryModel category = categoriesRepository.findById(skillDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Optional<SkillModel> existingSkillOpt = skillsRepository.findByNameIgnoreCase(skillDTO.getName());

        SkillModel skill;

        if (existingSkillOpt.isPresent()) {
            skill = existingSkillOpt.get();

            boolean alreadyInCategory = category.getSkills().stream()
                    .anyMatch(s -> s.getSkillId().equals(skill.getSkillId()));

            if (alreadyInCategory) {
                throw new SkillAlreadyExisted("Skill already exists in this category");
            }

            skill.getCategories().add(category);
            category.getSkills().add(skill);

        } else {
            skill = skillMapper.toEntity(skillDTO);
            skill.getCategories().add(category);
            category.getSkills().add(skill);
        }

        // CHỈ LƯU CATEGORY → skill mới sẽ tự động được persist nhờ cascade
        CategoryModel savedCategory = categoriesRepository.save(category);

        // Lấy skill đã được lưu (nếu là mới, nó đã có ID)
        SkillModel savedSkill = skill.getSkillId() != null ? skill :
                savedCategory.getSkills().stream()
                        .filter(s -> s.getName().equalsIgnoreCase(skillDTO.getName()))
                        .findFirst()
                        .orElse(skill);

        return skillMapper.toDTO(savedSkill);
    }

    @Override
    @Transactional
    public SkillDTO updateSkill(Long skillId, SkillDTO skillDTO) {
        SkillModel skillModel = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found")
        );

        if (skillDTO.getName() != null)
            skillModel.setName(skillDTO.getName());
        if (skillDTO.getDescription() != null)
            skillModel.setDescription(skillDTO.getDescription());

        SkillModel savedSkill = skillsRepository.save(skillModel);
        return skillMapper.toDTO(savedSkill);
    }

    @Override
    @Transactional
    public void deleteSkill(Long skillId) {
        if (!skillsRepository.existsById(skillId))
            throw new ResourceNotFoundException("Skill with id: " + skillId + " not found");
        skillsRepository.deleteById(skillId);
    }

    @Override
    public SkillDTO getSkillById(Long skillId) {
        SkillModel skill = skillsRepository.findById(skillId).orElseThrow(
                () -> new ResourceNotFoundException("Skill with id: " + skillId + " not found"));
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
                () -> new ResourceNotFoundException("Category with id: " + categoryId + " not found")
        );
        List<SkillModel> allskillsByCategory = skillsRepository.findAllByCategoriesContains(category);
        return skillMapper.toDTOs(allskillsByCategory);
    }

    @Override
    public List<SkillDTO> autoCompleteSearchSkill(String keyword, Pageable pageable) {
        Specification<SkillModel> spec = SkillSpecification.searchByName(keyword);
        return skillMapper.toDTOs(skillsRepository.findAll(spec, pageable).getContent());
    }
}
