package com.freelancemarketplace.backend.controller;

import com.freelancemarketplace.backend.auth.AppUser;
import com.freelancemarketplace.backend.dto.FreelancerDTO;
import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.dto.ResponseDTO;
import com.freelancemarketplace.backend.dto.SkillDTO;
import com.freelancemarketplace.backend.response.ResponseMessage;
import com.freelancemarketplace.backend.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.FreelancerService;
import com.freelancemarketplace.backend.service.SkillSerivice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/freelancers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }



    @PutMapping("/{freelancerId}")
    ResponseEntity<ResponseDTO> updateFreelancer(@PathVariable Long freelancerId,
                                                @RequestBody FreelancerDTO freelancerDTO){
        FreelancerDTO updatedFreelancer = freelancerService.updateFreelancer(freelancerId, freelancerDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedFreelancer
                ));
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAll() {
        List<FreelancerDTO> freelancerDTOs = freelancerService.getAllFreelancer();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        freelancerDTOs
                ));
    }

    @DeleteMapping("/{freelancerId}")
    ResponseEntity<ResponseDTO> deleteFreelancer(@PathVariable Long freelancerId) {
        freelancerService.deleteFreelancer(freelancerId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(ResponseStatusCode.NO_CONTENT,
                        ResponseMessage.NO_CONTENT));
    }

    @GetMapping("/getById/")
    public ResponseEntity<ResponseDTO>getSkillById(@AuthenticationPrincipal AppUser appUser){
        Long freelancerId = appUser.getId();
        FreelancerDTO givenFreelancer = freelancerService.getFreelancerById(freelancerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        givenFreelancer
                ));
    }


    @GetMapping("/profile/{freelancerId}") // 👈 Truyền ID qua URL
    public ResponseEntity<FreelancerDTO> getOtherFreelancerProfile(
            @PathVariable Long freelancerId) {

        // ************************************************
        // ⚠️ BƯỚC QUAN TRỌNG: Kiểm tra Quyền truy cập
        // ************************************************

        // Bạn phải thêm logic kiểm tra sau:
        // 1. Kiểm tra: Hồ sơ này có phải là hồ sơ công khai không? (Ví dụ: Chỉ trả về các trường công khai)
        // 2. Kiểm tra: Nếu người gọi là Client, họ có quyền xem hồ sơ đầy đủ của Freelancer này không? (Ví dụ: Đã thuê hoặc đang trong quá trình thương lượng)

        FreelancerDTO givenFreelancer = freelancerService.getFreelancerById(freelancerId);

        return ResponseEntity.ok(givenFreelancer);
    }

    @PutMapping("/assignSkillToFreelancer/freelancer/{freelancerId}/skill/{skillId}")
    public ResponseEntity<ResponseDTO>assignSkillToFreelancer(@PathVariable Long skillId,
                                                              @PathVariable Long freelancerId){
        FreelancerDTO updatedFreelancer = freelancerService.assignSkillToFreelancer(freelancerId,skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedFreelancer
                ));
    }

    @PutMapping("/removeSkillFromFreelancer/freelancer/{freelancerId}/skill/{skillId}")
    public ResponseEntity<ResponseDTO>removeSkillFromFreelancer(@PathVariable Long skillId,
                                                                @PathVariable Long freelancerId){
        FreelancerDTO updatedFreelancer = freelancerService.removeSkillFromFreelancer(freelancerId, skillId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        updatedFreelancer
                ));
    }
}
