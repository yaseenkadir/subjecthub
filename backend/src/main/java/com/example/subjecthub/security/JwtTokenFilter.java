package com.example.subjecthub.security;

import com.example.subjecthub.Application;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Checks valid jwts and if valid, assigns associated user to request for later use by controllers.
 */
public class JwtTokenFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private SecurityUserService subjectHubUserService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(AUTHORIZATION_HEADER);

        // TODO: Filter on requests only.
        if (authToken != null) {
            String username;
            try {
                username = jwtTokenUtils.getUsernameFromToken(authToken);
            } catch (JwtException|IllegalArgumentException e) {
                Application.log.error(e.getMessage(), e);
                username = null;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = subjectHubUserService.loadUserByUsername(username);
                if (userDetails != null && jwtTokenUtils.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
