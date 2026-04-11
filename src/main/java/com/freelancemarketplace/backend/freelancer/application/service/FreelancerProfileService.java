package com.freelancemarketplace.backend.freelancer.application.service;

import com.freelancemarketplace.backend.freelancer.dto.FreelancerDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application Service: Freelancer Profile Management (SRP)
 * This service handles freelancer profile operations including avatar upload
 */
public interface FreelancerProfileService {
    
    /**
     * Get freelancer profile
     * @param freelancerId Freelancer ID
     * @return Freelancer profile DTO
     */
    FreelancerDTO getProfile(Long freelancerId);
    
    /**
     * Update freelancer profile
     * @param freelancerId Freelancer ID
     * @param profile Updated profile data
     * @return Updated profile DTO
     */
    FreelancerDTO updateProfile(Long freelancerId, FreelancerDTO profile);
    
    /**
     * Upload freelancer avatar
     * @param freelancerId Freelancer ID
     * @param avatarFile Avatar image file
     * @return Updated profile with new avatar URL
     */
    FreelancerDTO uploadAvatar(Long freelancerId, MultipartFile avatarFile);
    
    /**
     * Delete freelancer avatar
     * @param freelancerId Freelancer ID
     */
    void deleteAvatar(Long freelancerId);
}

