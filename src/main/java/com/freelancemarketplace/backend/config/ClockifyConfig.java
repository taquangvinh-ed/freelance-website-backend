package com.freelancemarketplace.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ClockifyConfig {

    @Value("${CLOCKIFY_API_BASE_URL}")
    private String baseUrl;

    @Value("${CLOCKIFY_API_REPORTS_BASE_URL}")
    private String reportsBaseUrl;

    @Value("${CLOCKIFY_HEADER_API_KEY_NAME}")
    private String apiKeyHeaderName;

    @Value("${CLOCKIFY_WORKSPACE_DEFAULT_ID}")
    private String defaultWorkspaceId;

}
