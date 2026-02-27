package com.freelancemarketplace.backend.config;

import com.freelancemarketplace.backend.auth.CustomUsernamePasswordAuthenticationProvider;
import com.freelancemarketplace.backend.filter.JwtAuthenticationFilter;
import com.freelancemarketplace.backend.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Method;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.customUsernamePasswordAuthenticationProvider = customUsernamePasswordAuthenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authenticationProvider(customUsernamePasswordAuthenticationProvider)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(
                                "/api/users/",
                                "/api/categories/getAll",
                                "/api/skills/getAllSkill/Category/{categoryId}",
                                "/api/skills/getAll",
                                "/api/categories/getAll",
                                "/api/login", "/api/projects/getAllProjects",
                                "/api/projects/recommend/train-cf",

                                "/api/projects/filter",
                                "/api/skills/search", "/ws/**",
                                "/api/stripe/**",
                                "/api/stripe/webhook",
                                "/api/projects/autocomplete-search",
                                "/onboarding/**","/topic/notifications/**",
                                "/api/notifications/clarification-qa/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/proposals/{proposalId}/approve").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/contracts/{contractId}/milestones/{milestoneId}/pay").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/contracts/*/milestones/*/complete", "/api/contracts/milestone-attachment").hasRole("FREELANCER")
                        .requestMatchers("/api/upload/image", "/api/projects/recommend/freelancer/**", "/api/projects/recommend/client/**",
                                 "/api/chat/getContactInfo/**","/api/chat/getRecentConversation/",
                                "/api/projects/findProject/{projectId}",
                                 "/app/**", "/topic/chat/**", "/api/proposals/project/**",
                                "/api/messages/history/senderId/*/receiverId/*", "/api/chat/currentUserProfile",
                                "api/upload/file-chat", "/api/message/mark-as-read/**", "/api/proposals/find-by-freelancer-and-project/**",
                                    "/api/proposals/{proposalId}", "/api/contracts/get-contract/{contractId}", "/api/testimonials/**", "/api/experiences/**"
                               ,"/api/freelancers/profile/{freelancerId}", "/api/contracts/*/hourly-contract-logs", "/api/freelancers/info/*",
                                "/api/freelancers/upload-avatar", "/api/freelancers/me/upload-avatar" ).hasAnyRole("FREELANCER", "CLIENT", "ADMIN")
                        .requestMatchers("/api/freelancers/assignSkillToFreelancer/freelancer/*/skill/*",
                                "/api/freelancers/removeSkillFromFreelancer/freelancer/*/skill/*",
                                "/api/freelancers/{freelancerId}",
                                "/api/proposals/**", "/api/dashboard/freelancer/monthly-earnings", "/api/dashboard/freelancer/stats",
                                "/api/dashboard/freelancer/skillDistribution", "/api/dashboard/freelancer/recentClient",
                                "/api/dashboard/freelancer/projects/**", "/api/freelancers/getById/", "/api/certificate/**",
                                "/api/freelancers/me/status","/api/stripe/create-onboarding-link",
                                "/api/tracker/**"
                                
                        ).hasRole("FREELANCER")
                        .requestMatchers("/api/projects/", "/api/dashboard/client/**",  "/api/email/send-invitation").hasRole("CLIENT")
                        .requestMatchers("/api/categories/new-category", "/api/skills/new-skill", "/api/admin/**",
                                "/api/skills/new-skill-or-add-to-category").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(  jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Match your frontend origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Include OPTIONS for preflight
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow credentials (e.g., cookies, Authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;

    }

}
