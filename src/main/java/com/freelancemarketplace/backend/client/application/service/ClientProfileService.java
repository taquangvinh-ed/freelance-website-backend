package com.freelancemarketplace.backend.client.application.service;

import com.freelancemarketplace.backend.client.dto.ClientDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * Application Service: Client Profile Management (SRP)
 * This service handles client profile operations including avatar upload
 */
public interface ClientProfileService {
    
    /**
     * Get client profile
     * @param clientId Client ID
     * @return Client profile DTO
     */
    ClientDTO getProfile(Long clientId);
    
    /**
     * Update client profile
     * @param clientId Client ID
     * @param profile Updated profile data
     * @return Updated profile DTO
     */
    ClientDTO updateProfile(Long clientId, ClientDTO profile);
    
    /**
     * Upload client avatar
     * @param clientId Client ID
     * @param avatarFile Avatar image file
     * @return Updated profile with new avatar URL
     */
    ClientDTO uploadAvatar(Long clientId, MultipartFile avatarFile);
    
    /**
     * Delete client avatar
     * @param clientId Client ID
     */
    void deleteAvatar(Long clientId);
}

