package com.freelancemarketplace.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();

        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");

        if(cloudinaryUrl == null || cloudinaryUrl.isEmpty())
            throw  new IllegalStateException("Cloudinary URL is not set in .env file");

        try {
            return new Cloudinary(cloudinaryUrl);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize Cloudinary with provided URL", e);
        }
    }

}
