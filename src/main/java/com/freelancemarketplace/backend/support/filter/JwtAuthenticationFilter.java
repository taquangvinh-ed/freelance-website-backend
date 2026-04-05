package com.freelancemarketplace.backend.support.filter;

import com.freelancemarketplace.backend.infrastructure.security.auth.CustomUserDetailService;
import com.freelancemarketplace.backend.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailService customUserDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailService = customUserDetailService;
    }


    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/login",
            "/api/categories/getAll",
            "/api/skills/getAllSkill/Category/{categoryId}",
            "/api/skills/getAll",
            "/api/projects/getAllProjects",
            "/api/projects/recommend/train-cf",
            "/api/projects/filter",
            "/api/skills/search", "/ws/**",
            "/api/projects/autocomplete-search",
            "/api/stripe/**",
            "/onboarding/**",
            "/topic/notifications/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String uri = request.getRequestURI();
            // Remove query string if present
            if (uri.contains("?")) {
                uri = uri.substring(0, uri.indexOf("?"));
            }

            // Check if URI is in public endpoints (allow exact and wildcard matches)
            String finalUri = uri;
            String finalUri1 = uri;
            boolean isPublic = PUBLIC_ENDPOINTS.stream()
                    .anyMatch(endpoint -> {
                        if (endpoint.contains("**")) {
                            // Wildcard pattern: /api/stripe/**
                            String prefix = endpoint.replace("/**", "");
                            return finalUri.startsWith(prefix);
                        } else if (endpoint.contains("{")) {
                            // Path variable pattern: /api/skills/getAllSkill/Category/{categoryId}
                            String pattern = endpoint.replaceAll("\\{.*?}", "[^/]+");
                            return finalUri1.matches(pattern);
                        } else {
                            // Exact match
                            return finalUri.equals(endpoint);
                        }
                    });

            if (isPublic) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String email = jwtTokenProvider.getUserEmaildFromJwt(jwt);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Failed on set user authenticaiton ", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
