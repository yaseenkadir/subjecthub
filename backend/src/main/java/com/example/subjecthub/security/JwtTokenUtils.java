package com.example.subjecthub.security;

import com.example.subjecthub.Application;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Util class that manages jwt operations.
 */
@Component
@ParametersAreNonnullByDefault
public class JwtTokenUtils {

    private final String secretKey;

    public JwtTokenUtils(@Value("${com.example.subjecthub.jwt.secretKey}") String jwtSecretKey) {
        Application.log.info("Creating JwtTokenUtils");
        this.secretKey = jwtSecretKey;
    }

    public String getUsernameFromToken(String token) throws JwtException {
        return getClaimsFromToken(token).getSubject();
    }

    private Claims getClaimsFromToken(String token) throws JwtException {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    public String generateToken(String username) throws JwtException {
        return Jwts.builder()
            .setSubject(username)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws JwtException {
        // TODO: Add jwt expiry
        return getUsernameFromToken(token).equals(userDetails.getUsername());
    }
}

