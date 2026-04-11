package com.freelancemarketplace.backend.notification.application.port;

import org.springframework.web.multipart.MultipartFile;

/**
 * Port: Cloud Storage Provider Interface (OCP - Open/Closed Principle & Strategy Pattern)
 * Allows implementation of different cloud storage providers (Cloudinary, S3, etc.)
 */
public interface CloudStoragePort {
    
    /**
     * Upload file to cloud storage
     * @param file File to upload
     * @param folder Folder path in cloud storage
     * @return URL of uploaded file
     */
    String uploadFile(MultipartFile file, String folder);
    
    /**
     * Delete file from cloud storage
     * @param fileUrl URL or public ID of file to delete
     */
    void deleteFile(String fileUrl);
    
    /**
     * Get upload URL with constraints
     * @param folder Target folder
     * @param maxSize Max file size in bytes
     * @return Upload URL or configuration
     */
    String getUploadConfiguration(String folder, long maxSize);
}

