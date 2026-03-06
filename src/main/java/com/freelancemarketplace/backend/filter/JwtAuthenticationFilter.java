package com.freelancemarketplace.backend.filter;

import com.freelancemarketplace.backend.auth.CustomUserDetailService;
import com.freelancemarketplace.backend.jwt.JwtTokenProvider;
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
            "/api/categories/getAll",
            "/api/projects/getAllProjects",
            "/api/projects/recommend/train-cf",
            "/api/projects/filter",
            "/api/skills/search", "/ws/**",
            "/api/projects/autocomplete-search",
            "/api/stripe/**",
            "/onboarding/**",
            "/topic/notifications/**",
            "/api/ai/project-assistant/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.debug("🔐 JWT Filter processing: {}", requestURI);

        try {
            if (PUBLIC_ENDPOINTS.contains(requestURI)) {
                log.debug("✅ Public endpoint, skipping JWT validation: {}", requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = getJwtFromRequest(request);
            log.debug("🔑 JWT extracted: {}", jwt != null ? "YES (length: " + jwt.length() + ")" : "NO");

            if (StringUtils.hasText(jwt)) {
                boolean isValid = jwtTokenProvider.validateToken(jwt);
                log.debug("🔍 Token validation result: {}", isValid);

                if (isValid) {
                    String email = jwtTokenProvider.getUserEmaildFromJwt(jwt);
                    log.debug("📧 Email from JWT: {}", email);

                    UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
                    log.debug("👤 User loaded: {}", userDetails != null ? userDetails.getUsername() : "NULL");

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("✅ Authentication set in SecurityContext for user: {}", email);
                    } else {
                        log.warn("❌ UserDetails is null for email: {}", email);
                    }
                } else {
                    log.warn("❌ Token validation failed");
                }
            } else {
                log.warn("❌ No JWT token found in request to: {}", requestURI);
            }
        } catch (Exception e) {
            log.error("❌ Failed to set user authentication for URI: {}", requestURI, e);
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
