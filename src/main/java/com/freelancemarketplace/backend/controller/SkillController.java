package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.repository.SkillsRepository;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.SkillSerivice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SkillController {

    private final SkillSerivice skillSerivice;

    public SkillController(SkillSerivice skillSerivice) {
        this.skillSerivice = skillSerivice;
    }

    @PostMapping("/")
    ResponseEntity<ResponseDTO>createSkill(@RequestBody SkillDTO skillDTO){
        SkillDTO newSkill = skillSerivice.createSkill(skillDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.CREATED,
                        ResponseMessage.CREATED,
                        newSkill
                ));
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<ResponseDTO>updateSkill(@PathVariable Long skillId,
                                                  @RequestBody SkillDTO skillDTO){
        SkillDTO updatedSkill = skillSerivice.updateSkill(skillId, skillDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedSkill
                ));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<ResponseDTO>deleteSkill(@PathVariable Long skillId){
        skillSerivice.deleteSkill(skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS
                                        ));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO>getAllSkill(){
        List<SkillDTO> skillDTOs = skillSerivice.getAllSkill();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        skillDTOs
                ));
    }

    @GetMapping("/getById/{skillId}")
    public ResponseEntity<ResponseDTO>getSkillById(@PathVariable Long skillId){
        SkillDTO givenSkill = skillSerivice.getSkillById(skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        givenSkill
                ));
    }





    @GetMapping("/getAllSkill/Category/{categoryId}")
    ResponseEntity<ResponseDTO>getAllSkillByCategory(@PathVariable Long categoryId){
        List<SkillDTO> skills = skillSerivice.getAllSkillByCategory(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        skills));
    }
}
