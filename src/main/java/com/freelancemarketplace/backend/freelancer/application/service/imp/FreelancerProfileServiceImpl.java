package com.freelancemarketplace.backend.freelancer.application.service.imp;

import com.freelancemarketplace.backend.notification.application.port.CloudStoragePort;
import com.freelancemarketplace.backend.freelancer.application.service.FreelancerProfileService;
import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import com.freelancemarketplace.backend.exceptionHandling.BaseApplicationException;
import com.freelancemarketplace.backend.exceptionHandling.ErrorCode;
import com.freelancemarketplace.backend.infrastructure.mapper.FreelancerMapper;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation: Freelancer Profile Management (SRP)
 * 
 * Demonstrates:
 * - DIP: Depends on CloudStoragePort (abstraction), not concrete Cloudinary service
 * - SRP: Only handles freelancer profile operations
 * - OCP: Easy to switch cloud storage providers by changing CloudStoragePort binding
 */
@Service
@AllArgsConstructor
@Slf4j
public class FreelancerProfileServiceImpl implements FreelancerProfileService {
    
    private final FreelancersRepository freelancerRepository;
    private final FreelancerMapper freelancerMapper;
    private final CloudStoragePort cloudStoragePort;
    
    @Override
    public FreelancerDTO getProfile(Long freelancerId) {
        log.info("Getting freelancer profile: {}", freelancerId);
        
        FreelancerModel freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Freelancer not found with id: " + freelancerId
                ));
        
        return freelancerMapper.toDTO(freelancer);
    }
    
    @Override
    @Transactional
    public FreelancerDTO updateProfile(Long freelancerId, FreelancerDTO profile) {
        log.info("Updating freelancer profile: {}", freelancerId);
        
        FreelancerModel freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Freelancer not found with id: " + freelancerId
                ));
        
        // Partial update
        freelancerMapper.partialUpdate(profile, freelancer);
        
        FreelancerModel updatedFreelancer = freelancerRepository.save(freelancer);
        
        log.info("Freelancer profile updated successfully");
        
        return freelancerMapper.toDTO(updatedFreelancer);
    }
    
    @Override
    @Transactional
    public FreelancerDTO uploadAvatar(Long freelancerId, MultipartFile avatarFile) {
        log.info("Uploading avatar for freelancer: {}", freelancerId);
        
        // Validate file
        if (avatarFile == null || avatarFile.isEmpty()) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Avatar file is required"
            );
        }
        
        // Validate file size (5MB max)
        long maxFileSize = 5 * 1024 * 1024; // 5MB
        if (avatarFile.getSize() > maxFileSize) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Avatar file size exceeds maximum limit of 5MB"
            );
        }
        
        // Validate content type
        String contentType = avatarFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BaseApplicationException(
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Only image files are allowed"
            );
        }
        
        // Find freelancer
        FreelancerModel freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Freelancer not found with id: " + freelancerId
                ));
        
        // Delete old avatar if exists
        if (freelancer.getAvatar() != null && !freelancer.getAvatar().isEmpty()) {
            try {
                cloudStoragePort.deleteFile(freelancer.getAvatar());
                log.info("Old avatar deleted");
            } catch (Exception e) {
                log.warn("Failed to delete old avatar", e);
                // Continue with upload even if delete fails
            }
        }
        
        // Upload new avatar
        String avatarUrl = cloudStoragePort.uploadFile(avatarFile, "avatars/freelancers");
        
        // Update freelancer
        freelancer.setAvatar(avatarUrl);
        FreelancerModel updatedFreelancer = freelancerRepository.save(freelancer);
        
        log.info("Avatar uploaded successfully: {}", avatarUrl);
        
        return freelancerMapper.toDTO(updatedFreelancer);
    }
    
    @Override
    @Transactional
    public void deleteAvatar(Long freelancerId) {
        log.info("Deleting avatar for freelancer: {}", freelancerId);
        
        FreelancerModel freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new BaseApplicationException(
                        ErrorCode.USER_NOT_FOUND,
                        HttpStatus.NOT_FOUND,
                        "Freelancer not found with id: " + freelancerId
                ));
        
        if (freelancer.getAvatar() != null && !freelancer.getAvatar().isEmpty()) {
            try {
                cloudStoragePort.deleteFile(freelancer.getAvatar());
                freelancer.setAvatar(null);
                freelancerRepository.save(freelancer);
                log.info("Avatar deleted successfully");
            } catch (Exception e) {
                log.error("Error deleting avatar", e);
                throw new BaseApplicationException(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to delete avatar"
                );
            }
        }
    }
}

