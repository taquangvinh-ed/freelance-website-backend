package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.dto.SkillDTO2;
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

import java.util.HashSet;
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
//        CategoryModel category = categoriesRepository.findById(skillDTO.getCategoryId())
//                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
//
//        Optional<SkillModel> existingSkillOpt = skillsRepository.findByNameIgnoreCase(skillDTO.getName());
//
//        SkillModel skill;
//
//        if (existingSkillOpt.isPresent()) {
//            skill = existingSkillOpt.get();
//
//            boolean alreadyInCategory = category.getSkills().stream()
//                    .anyMatch(s -> s.getSkillId().equals(skill.getSkillId()));
//
//            if (alreadyInCategory) {
//                throw new SkillAlreadyExisted("Skill already exists in this category");
//            }
//
//            skill.getCategories().add(category);
//            category.getSkills().add(skill);
//
//        } else {
//            skill = skillMapper.toEntity(skillDTO);
//            skill.getCategories().add(category);
//            category.getSkills().add(skill);
//        }
//
//        // CHỈ LƯU CATEGORY → skill mới sẽ tự động được persist nhờ cascade
//        CategoryModel savedCategory = categoriesRepository.save(category);
//
//        // Lấy skill đã được lưu (nếu là mới, nó đã có ID)
//        SkillModel savedSkill = skill.getSkillId() != null ? skill :
//                savedCategory.getSkills().stream()
//                        .filter(s -> s.getName().equalsIgnoreCase(skillDTO.getName()))
//                        .findFirst()
//                        .orElse(skill);
//
//        return skillMapper.toDTO(savedSkill);
        return null;
    }


    @Override
    public SkillDTO2 createSkillOrAddToCategories(SkillDTO2 skillDTO2) {
        // 1. Lấy tất cả CategoryModels dựa trên IDs
        List<CategoryModel> categories = categoriesRepository.findAllById(skillDTO2.getCategoryIds());

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found with the provided IDs.");
        }

        // 2. Tìm kiếm Skill hiện có (chỉ dựa trên tên)
        Optional<SkillModel> existingSkillOpt = skillsRepository.findByNameIgnoreCase(skillDTO2.getName());
        SkillModel skill;
        boolean isNewSkill = !existingSkillOpt.isPresent();

        if (isNewSkill) {
            // Tạo Skill mới nếu chưa tồn tại
            skill = skillMapper.toEntity2(skillDTO2);
            // Quan trọng: Khởi tạo Set<CategoryModel> nếu nó có trong SkillModel để tránh NPE
            skill.setCategories(new HashSet<>());
        } else {
            skill = existingSkillOpt.get();
        }

        // 3. Xử lý liên kết Skill với từng Category (Cập nhật Owning Side)
        for (CategoryModel category : categories) {

            // 3a. Kiểm tra xem Skill đã tồn tại trong Category này chưa (Kiểm tra Owning Side)
            SkillModel finalSkill = skill;
            boolean alreadyInCategory = category.getSkills().stream()
                    .anyMatch(s -> s.getSkillId() != null && s.getSkillId().equals(finalSkill.getSkillId()));

            if (alreadyInCategory) {
                continue; // Bỏ qua nếu đã liên kết
            }

            // 3b. Thiết lập mối quan hệ từ cả hai phía để đồng bộ trạng thái Java
            // Owning Side (Category) thực hiện thay đổi CSDL:
            category.getSkills().add(skill);

            // Phía ngược lại (Skill) chỉ thay đổi trong bộ nhớ:
            skill.getCategories().add(category);
        }

        // 4. LƯU (PERSIST) CÁC THAY ĐỔI

        // Nếu là Skill mới, bạn phải lưu nó trước để có ID
        if (isNewSkill) {
            skill = skillsRepository.save(skill);
            // Sau khi lưu Skill mới, mối quan hệ vẫn chưa được cập nhật trong CSDL vì Category là owning side.
            // Tiếp tục lưu Category để Hibernate update join table.
        }

        // Lưu tất cả các Category đã được cập nhật
        List<CategoryModel> savedCategories = categoriesRepository.saveAll(categories);

        // Nếu bạn muốn lấy lại Skill đã được liên kết với các Category,
        // bạn cần tải lại Category hoặc Skill, nhưng cách đơn giản nhất là dùng skill đã có.

        // 5. Trả về DTO (Sử dụng SkillModel đã được lưu)
        return skillMapper.toDTO2(skill);
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
