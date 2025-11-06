package com.freelancemarketplace.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ClockifyConfig {

    @Value("${clockify.api.key}")
    private String key;

    @Value("${clockify.api.base_url}")
    private String baseUrl;

    @Value("${clockify.reports.base_url}")
    private String reportsBaseUrl;

    @Value("${clockify.api.header.key_name}")
    private String apiKeyHeaderName;

    @Value("${clockify.workspace.default_id}")
    private String defaultWorkspaceId;

}
