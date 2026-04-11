package com.freelancemarketplace.backend.skill.api.controller;

import com.freelancemarketplace.backend.common.api.response.ApiResponse;
import com.freelancemarketplace.backend.skill.dto.SkillDTO;
import com.freelancemarketplace.backend.skill.dto.SkillDTO2;
import com.freelancemarketplace.backend.skill.application.service.SkillSerivice;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SkillController {

    private final SkillSerivice skillSerivice;

    public SkillController(SkillSerivice skillSerivice) {
        this.skillSerivice = skillSerivice;
    }

    @PostMapping("/new-skill")
    ApiResponse<?> createSkill(@RequestBody SkillDTO skillDTO){
        SkillDTO newSkill = skillSerivice.createSkill(skillDTO);
        return ApiResponse.created(newSkill);
    }

    @PostMapping("/new-skill-or-add-to-category")
    ApiResponse<?> createSkillOrAddToCategories(@RequestBody SkillDTO2 skillDTO){
        SkillDTO2 newSkill = skillSerivice.createSkillOrAddToCategories(skillDTO);
        return ApiResponse.success(newSkill);
    }

    @PatchMapping("/{skillId}")
    public ApiResponse<?> updateSkill(@PathVariable Long skillId,
                                                  @RequestBody SkillDTO2 skillDTO){
        SkillDTO updatedSkill = skillSerivice.updateSkill(skillId, skillDTO);
        return ApiResponse.success(updatedSkill);
    }

    @DeleteMapping("/{skillId}")
    public ApiResponse<?> deleteSkill(@PathVariable Long skillId){
        skillSerivice.deleteSkill(skillId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getAll")
    public ApiResponse<?> getAllSkill(){
        List<SkillDTO> skillDTOs = skillSerivice.getAllSkill();
        return ApiResponse.success(skillDTOs);
    }

    @GetMapping("/getById/{skillId}")
    public ApiResponse<?> getSkillById(@PathVariable Long skillId){
        SkillDTO givenSkill = skillSerivice.getSkillById(skillId);
        return ApiResponse.success(givenSkill);
    }





    @GetMapping("/getAllSkill/Category/{categoryId}")
    ApiResponse<?> getAllSkillByCategory(@PathVariable Long categoryId){
        List<SkillDTO> skills = skillSerivice.getAllSkillByCategory(categoryId);
        return ApiResponse.success(skills);
    }

    @GetMapping("/search")
    ApiResponse<?> searchSkill(@RequestParam(required = false) String keyword, Pageable pageable){
        List<SkillDTO> skills = skillSerivice.autoCompleteSearchSkill(keyword, pageable);
        return ApiResponse.success(skills);
    }
}
