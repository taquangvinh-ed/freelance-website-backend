package com.freelancemarketplace.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TogglConfig {

    @Value("${toggl.api_key}")
    private String apiKey;

    @Value("${toggl.workspace.id}")
    private String defaultWorkspaceId;

    @Value("${toggl.base_url}")
    private String baseUrl;

    @Value("${toggl.organization_id}")
    private String organizationId;
}
