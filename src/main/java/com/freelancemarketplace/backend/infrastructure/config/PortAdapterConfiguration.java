package com.freelancemarketplace.backend.infrastructure.config;

import com.freelancemarketplace.backend.project.application.port.ProjectCrudPort;
import com.freelancemarketplace.backend.client.application.port.ClientCrudPort;
import com.freelancemarketplace.backend.freelancer.application.port.FreelancerCrudPort;
import com.freelancemarketplace.backend.notification.application.port.CloudStoragePort;
import com.freelancemarketplace.backend.notification.application.port.EmailPort;
import com.freelancemarketplace.backend.infrastructure.adapter.CloudinaryStorageAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.ClientRepositoryAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.FreelancerRepositoryAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.ProjectRepositoryAdapter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortAdapterConfiguration {
    
    @Bean
    public ProjectCrudPort projectCrudPort(ProjectRepositoryAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public FreelancerCrudPort freelancerCrudPort(FreelancerRepositoryAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public ClientCrudPort clientCrudPort(ClientRepositoryAdapter adapter) {
        return adapter;
    }
    
    @Bean
    public CloudStoragePort cloudStoragePort(CloudinaryStorageAdapter cloudinaryAdapter) {
        return cloudinaryAdapter;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
