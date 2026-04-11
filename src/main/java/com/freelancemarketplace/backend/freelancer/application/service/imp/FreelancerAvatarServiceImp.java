package com.freelancemarketplace.backend.freelancer.application.service.imp;

import com.freelancemarketplace.backend.freelancer.application.service.FreelancerAvatarService;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.project.application.service.CloudinaryService;
import com.freelancemarketplace.backend.exceptionHandling.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreelancerAvatarServiceImp implements FreelancerAvatarService {

    private final FreelancersRepository freelancersRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public String uploadAvatar(Long freelancerId, MultipartFile file) throws IOException {
        FreelancerModel freelancer = freelancersRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer not found with id: " + freelancerId));

        String avatarUrl = cloudinaryService.uploadAvatar(file);
        freelancer.setAvatar(avatarUrl);
        freelancersRepository.save(freelancer);

        log.info("Avatar uploaded successfully for freelancer: {}", freelancerId);
        return avatarUrl;
    }
}
