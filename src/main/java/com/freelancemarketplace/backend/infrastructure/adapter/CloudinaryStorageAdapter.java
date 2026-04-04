package com.freelancemarketplace.backend.infrastructure.adapter;

import com.freelancemarketplace.backend.application.port.CloudStoragePort;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Adapter: Cloudinary Cloud Storage Implementation (Strategy Pattern - OCP)
 * Implements CloudStoragePort for Cloudinary
 * Can be swapped with S3CloudStorageAdapter or AzureBlobStorageAdapter without changing business logic
 */
@Component("cloudinaryStorageAdapter")
@Slf4j
@AllArgsConstructor
public class CloudinaryStorageAdapter implements CloudStoragePort {
    
    private final Cloudinary cloudinary;
    
    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            log.info("Uploading file to Cloudinary folder: {}", folder);
            
            Map uploadResult = cloudinary.uploader().upload(
                    file.getInputStream(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto",
                            "use_filename", true,
                            "unique_filename", true
                    )
            );
            
            String fileUrl = (String) uploadResult.get("secure_url");
            log.info("File uploaded successfully: {}", fileUrl);
            
            return fileUrl;
        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    @Override
    public void deleteFile(String fileUrl) {
        try {
            log.info("Deleting file from Cloudinary: {}", fileUrl);
            
            // Extract public ID from URL
            String publicId = extractPublicId(fileUrl);
            
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            log.info("File deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting file from Cloudinary", e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }
    
    @Override
    public String getUploadConfiguration(String folder, long maxSize) {
        // Return upload configuration or signature for direct upload
        // This can be used for client-side uploads
        log.info("Getting upload configuration for folder: {}", folder);
        
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,
                "max_file_size", maxSize,
                "resource_type", "image"
        );
        
        // Return as JSON string or generate signature
        return uploadParams.toString();
    }
    
    /**
     * Extract public ID from Cloudinary URL
     */
    private String extractPublicId(String fileUrl) {
        // Extract public ID from URL like:
        // https://res.cloudinary.com/[cloud]/image/upload/v[version]/[folder]/[public_id].ext
        if (fileUrl == null || fileUrl.isEmpty()) {
            return fileUrl;
        }
        
        String[] parts = fileUrl.split("/");
        String lastPart = parts[parts.length - 1];
        return lastPart.substring(0, lastPart.lastIndexOf("."));
    }
}

