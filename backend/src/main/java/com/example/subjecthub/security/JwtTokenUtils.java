package com.example.subjecthub.security;

import com.example.subjecthub.Application;
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

    public String generateToken(String username) throws JwtException {
        return generateToken(username, null,
            Date.from(ZonedDateTime.now().plusWeeks(1).toInstant()));
    }

    public String generateToken(String username, @Nullable Map<String, Object> claims, Date expiry)
        throws JwtException {
        JwtBuilder builder = Jwts.builder()
            .setSubject(username)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .setExpiration(expiry);

        if (claims != null) {
            builder.setClaims(claims);
        }
        return builder.compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) throws JwtException {
        if (!getTokenExpiry(token).after(new Date())) {
            throw new JwtException("Token has expired. Authenticate again.");
        }
        return getUsernameFromToken(token).equals(userDetails.getUsername());
    }
}

