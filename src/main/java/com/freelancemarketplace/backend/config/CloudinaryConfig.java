package com.freelancemarketplace.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary getCloudinaryConfig(){
        String cloudinaryUrl = System.getenv("CLOUDINARY_URL");
        return new Cloudinary(cloudinaryUrl);
    }

}
