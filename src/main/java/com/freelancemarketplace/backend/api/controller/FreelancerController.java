package com.freelancemarketplace.backend.api.controller;

import com.freelancemarketplace.backend.infrastructure.security.auth.AppUser;
import com.freelancemarketplace.backend.dto.*;
import com.freelancemarketplace.backend.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.api.response.ResponseMessage;
import com.freelancemarketplace.backend.api.response.ResponseStatusCode;
import com.freelancemarketplace.backend.service.FreelancerService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerInfoDTO;

@RestController
@RequestMapping(path = "/api/freelancers", produces = {MediaType.APPLICATION_JSON_VALUE})
public class FreelancerController {

    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }



    @PutMapping("/{freelancerId}")
    ApiResponse<?> updateFreelancer(@PathVariable Long freelancerId,
                                                @RequestBody FreelancerDTO freelancerDTO){
        FreelancerDTO updatedFreelancer = freelancerService.updateFreelancer(freelancerId, freelancerDTO);
        return ApiResponse.success(updatedFreelancer);
    }

    @GetMapping("/getAll")
    public ApiResponse<?> getAll() {
        List<FreelancerDTO> freelancerDTOs = freelancerService.getAllFreelancer();
        return ApiResponse.success(freelancerDTOs);
    }

    @DeleteMapping("/{freelancerId}")
    ApiResponse<?> deleteFreelancer(@PathVariable Long freelancerId) {
        freelancerService.deleteFreelancer(freelancerId);
        return ApiResponse.noContent();
    }

    @GetMapping("/getById/")
    public ApiResponse<?> getSkillById(@AuthenticationPrincipal AppUser appUser){
        Long freelancerId = appUser.getId();
        FreelancerDTO givenFreelancer = freelancerService.getFreelancerById(freelancerId);
        return ApiResponse.success(givenFreelancer);
    }


    @GetMapping("/profile/{freelancerId}") // 👈 Truyền ID qua URL
    public ApiResponse<?> getOtherFreelancerProfile(
            @PathVariable Long freelancerId) {

        // ************************************************
        // ⚠️ BƯỚC QUAN TRỌNG: Kiểm tra Quyền truy cập
        // ************************************************

        // Bạn phải thêm logic kiểm tra sau:
        // 1. Kiểm tra: Hồ sơ này có phải là hồ sơ công khai không? (Ví dụ: Chỉ trả về các trường công khai)
        // 2. Kiểm tra: Nếu người gọi là Client, họ có quyền xem hồ sơ đầy đủ của Freelancer này không? (Ví dụ: Đã thuê hoặc đang trong quá trình thương lượng)

        FreelancerDTO givenFreelancer = freelancerService.getFreelancerById(freelancerId);

        return ApiResponse.success(givenFreelancer);
    }

    @PutMapping("/assignSkillToFreelancer/freelancer/{freelancerId}/skill/{skillId}")
    public ApiResponse<?> assignSkillToFreelancer(@PathVariable Long skillId,
                                                              @PathVariable Long freelancerId){
        FreelancerDTO updatedFreelancer = freelancerService.assignSkillToFreelancer(freelancerId,skillId);
        return ApiResponse.success(updatedFreelancer);
    }

    @PutMapping("/removeSkillFromFreelancer/freelancer/{freelancerId}/skill/{skillId}")
    public ApiResponse<?> removeSkillFromFreelancer(@PathVariable Long skillId,
                                                                @PathVariable Long freelancerId){
        FreelancerDTO updatedFreelancer = freelancerService.removeSkillFromFreelancer(freelancerId, skillId);
        return ApiResponse.success(updatedFreelancer);
    }

    @GetMapping("/me/status")
    public ApiResponse<?> status(@AuthenticationPrincipal AppUser appUser) throws StripeException {
        Long freelancerId = appUser.getId();
        FreelancerModel freelancer = freelancerService.findById(freelancerId);

        String stripeAccountId = freelancer.getStripeAccountId();
        Account account = Account.retrieve(stripeAccountId);
        boolean completed = account.getChargesEnabled() && account.getPayoutsEnabled();
        if(completed){
            freelancerService.markOnboardingCompleted(stripeAccountId);
        }
        return ApiResponse.success(Map.of(
                "onboardingCompleted", completed,
                "stripeAccountId", stripeAccountId,
                "chargesEnabled", account.getChargesEnabled(),
                "payoutsEnabled", account.getPayoutsEnabled()
        ));
    }

    @GetMapping("/info/{freelancerId}")
    public ApiResponse<?> getInfo(@PathVariable Long freelancerId){
        FreelancerInfoDTO info = freelancerService.getInfo(freelancerId);
        return ApiResponse.success(info);
    }

    @PostMapping("/upload-avatar")
    public ApiResponse<?> uploadAvatar(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam MultipartFile file) throws IOException {
        Long freelancerId = appUser.getId();
        String avatarUrl = freelancerService.uploadAvatar(freelancerId, file);
        return ApiResponse.success(Map.of("avatarUrl", avatarUrl));
    }

    @PostMapping("/me/upload-avatar")
    public ApiResponse<?> uploadMyAvatar(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam MultipartFile file) throws IOException {
        Long freelancerId = appUser.getId();
        String avatarUrl = freelancerService.uploadAvatar(freelancerId, file);
        return ApiResponse.success(Map.of("avatarUrl", avatarUrl));
    }
}
