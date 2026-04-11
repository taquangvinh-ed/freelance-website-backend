package com.freelancemarketplace.backend.infrastructure.config;

import com.freelancemarketplace.backend.project.application.port.ProjectCrudPort;
import com.freelancemarketplace.backend.client.application.port.ClientCrudPort;
import com.freelancemarketplace.backend.freelancer.application.port.FreelancerCrudPort;
import com.freelancemarketplace.backend.notification.application.port.CloudStoragePort;
import com.freelancemarketplace.backend.notification.application.port.EmailPort;
import com.freelancemarketplace.backend.recommendation.application.port.LLMPort;
import com.freelancemarketplace.backend.infrastructure.adapter.ClaudeLLMAdapter;
import com.freelancemarketplace.backend.infrastructure.adapter.CloudinaryStorageAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.ClientRepositoryAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.FreelancerRepositoryAdapter;
import com.freelancemarketplace.backend.infrastructure.persistence.ProjectRepositoryAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Class: Dependency Injection Management (DIP - Dependency Inversion Principle)
 * 
 * Centralized location for:
 * - Port-to-Adapter bindings
 * - Bean creation and configuration
 * - Strategy selection (which implementation to use)
 * 
 * Benefits:
 * - Single place to change implementations
 * - No scattered @Autowired across codebase
 * - Easy to test with different implementations
 * - Supports multiple implementations via @Qualifier
 */
@Configuration
public class PortAdapterConfiguration {
    
    // ==================== Repository Ports ====================
    
    /**
     * Bind ProjectCrudPort to ProjectRepositoryAdapter
     */
    @Bean
    public ProjectCrudPort projectCrudPort(ProjectRepositoryAdapter adapter) {
        return adapter;
    }
    
    /**
     * Bind FreelancerCrudPort to FreelancerRepositoryAdapter
     */
    @Bean
    public FreelancerCrudPort freelancerCrudPort(FreelancerRepositoryAdapter adapter) {
        return adapter;
    }
    
    /**
     * Bind ClientCrudPort to ClientRepositoryAdapter
     */
    @Bean
    public ClientCrudPort clientCrudPort(ClientRepositoryAdapter adapter) {
        return adapter;
    }
    
    // ==================== External Service Ports ====================
    
    /**
     * Bind LLMPort to ClaudeLLMAdapter
     * 
     * To switch to a different provider:
     * 1. Create new adapter (e.g., OpenAILLMAdapter)
     * 2. Change this bean to return the new adapter
     * 3. No changes needed in business logic layer
     */
    @Bean
    public LLMPort llmPort(ClaudeLLMAdapter claudeAdapter) {
        return claudeAdapter;
    }
    
    /**
     * Bind CloudStoragePort to CloudinaryStorageAdapter
     * 
     * To switch to S3:
     * 1. Create S3CloudStorageAdapter implementing CloudStoragePort
     * 2. Change this bean to return S3CloudStorageAdapter
     * 3. No changes needed in business logic layer
     */
    @Bean
    public CloudStoragePort cloudStoragePort(CloudinaryStorageAdapter cloudinaryAdapter) {
        return cloudinaryAdapter;
    }
    
    // ==================== Utility Beans ====================
    
    /**
     * ObjectMapper for JSON serialization
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
