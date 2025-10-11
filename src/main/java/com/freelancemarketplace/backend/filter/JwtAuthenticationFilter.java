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
            "/api/stripe/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            if (PUBLIC_ENDPOINTS.contains(request.getRequestURI())) {
                filterChain.doFilter(request, response); // Skip JWT validation for login
                return;
            }

            String jwt = getJwtFromRequest(request);

            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                String email = jwtTokenProvider.getUserEmaildFromJwt(jwt);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

                if(userDetails != null ){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }catch (Exception e){
            log.error("Failed on set user authenticaiton ", e);
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
