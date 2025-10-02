package com.freelancemarketplace.backend.jwt;

import com.freelancemarketplace.backend.auth.AppUser;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String JWT_SECRET = "7b6b5b4a3c2d1e0f9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d3e2f1a0b9c8d7e6f";
    private final Long JWT_EXPIRATION = 60480000L;


    public String genarateToken(AppUser appUser){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", appUser.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse(""));


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(appUser.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

    }

    public String getUserEmaildFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getUserRoleFromJwt(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        }catch (RuntimeException e){
            log.error("Failed to parse role from JWT {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public Boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).build().parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException e){
            log.error("Invalid Jwt Token");
        }catch (ExpiredJwtException e){
            log.error("Expired Jwt Token");
        }catch (UnsupportedJwtException e){
            log.error("Unsupported jwt token");
        }catch (IllegalArgumentException e){
            log.error("Jwt claims string is empty");
        }
        return false;
    }


}
