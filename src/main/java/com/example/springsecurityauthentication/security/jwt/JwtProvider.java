package com.example.springsecurityauthentication.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.expiration}")
    private int jwtExpiration;
    private final Key key;
    public JwtProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            logger.error("Invalid JWT signature -> Message: {}", e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
        } catch (io.jsonwebtoken.JwtException e) {
            logger.error("JWT exception -> Message: {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
