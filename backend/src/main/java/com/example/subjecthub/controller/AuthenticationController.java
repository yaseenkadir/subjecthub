package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.dto.AuthenticationRequest;
import com.example.subjecthub.dto.RegisterRequest;
import com.example.subjecthub.dto.SubjectHubUserResponse;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.security.JwtTokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> authenticate(
        @RequestBody AuthenticationRequest authRequest
    ) {
        Application.log.info("Attempting authentication for {}", authRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException ae) {
            return ResponseEntity.badRequest().body(
                Collections.singletonMap("error", "Invalid credentials"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtTokenUtils.generateToken(authRequest.getUsername()));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(
        @RequestBody RegisterRequest registerRequest
    ) {
        String hashedPassword = bCryptPasswordEncoder.encode(registerRequest.getPassword());
        SubjectHubUser subjectHubUser = new SubjectHubUser(registerRequest.getUsername(),
            hashedPassword, registerRequest.getEmail());
        subjectHubUserRepository.save(subjectHubUser);
        return ResponseEntity.ok(null);
    }

    /**
     * Returns details of the user.
     *
     * Might remove this if not needed.
     */
    @RequestMapping(value = "/self", method = RequestMethod.GET)
    public ResponseEntity self() {
        UserDetails userDetails;
        try {
            userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException|ClassCastException e) {
            // TODO: Throw appropriate exception
            Application.log.error("No user found");
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",
                "Not logged in"));
        }

        Optional<SubjectHubUser> user = subjectHubUserRepository.findByUsername(
            userDetails.getUsername());
        if (user.isPresent()) {
            SubjectHubUserResponse response = new SubjectHubUserResponse();
            BeanUtils.copyProperties(user.get(), response);
            return ResponseEntity.ok(response);
        } else {
            Application.log.error("User is authenticated but is not found in database!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Collections.singletonMap("error", "Unexpected error"));
        }
    }
}
