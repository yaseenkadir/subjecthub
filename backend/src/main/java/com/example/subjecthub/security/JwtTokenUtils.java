package com.example.subjecthub.security;

import com.example.subjecthub.Application;
import com.example.subjecthub.entity.SubjectHubUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class that manages jwt operations.
 */
@Component
@ParametersAreNonnullByDefault
public class JwtTokenUtils {

    private final byte[] secretKey;

    public JwtTokenUtils(@Value("${com.example.subjecthub.jwt.secretKey}") String jwtSecretKey) {
        Application.log.info("Creating JwtTokenUtils");
        this.secretKey = jwtSecretKey.getBytes();
    }

    public String getUsernameFromToken(String token) throws JwtException {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getTokenExpiry(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public Claims getClaimsFromToken(String token) throws JwtException {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }

    public String generateToken(SubjectHubUser user) {
        Application.log.info("Generating token for {}", user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("admin", user.getAdmin());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());
        return generateToken(user.getUsername(), claims,
            Date.from(ZonedDateTime.now().plusWeeks(1).toInstant()));
    }

    public String generateToken(String subject, @Nullable Map<String, Object> claims, Date expiry)
        throws JwtException {

        if (claims == null) {
            claims = new HashMap<>();
        }

        JwtBuilder builder = Jwts.builder();
        return builder
            .setClaims(claims)
            // set subject after claims or else setting claims will overwrite subject
            // 2+ hours debugging this...
            .setSubject(subject)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(expiry)
            .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws JwtException {
        if (!getTokenExpiry(token).after(new Date())) {
            throw new JwtException("Token has expired. Authenticate again.");
        }
        return getUsernameFromToken(token).equals(userDetails.getUsername());
    }
}

