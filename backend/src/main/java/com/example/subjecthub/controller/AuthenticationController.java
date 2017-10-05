package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.dto.AuthenticationRequest;
import com.example.subjecthub.dto.JwtResponse;
import com.example.subjecthub.dto.RegisterRequest;
import com.example.subjecthub.dto.SubjectHubUserResponse;
import com.example.subjecthub.entity.SubjectHubUser;
import com.example.subjecthub.repository.SubjectHubUserRepository;
import com.example.subjecthub.security.JwtTokenUtils;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.exception.SubjectHubUnexpectedException;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SubjectHubUserRepository subjectHubUserRepository;

    // Username must start with a letter and can be followed by letters or numbers. Between 5-20
    // characters.
    private static final Pattern usernamePattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9]{4,19}");

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public JwtResponse authenticate(
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
            throw new SubjectHubException("Invalid credentials.");
        }

        Application.log.info("{} logged in.", authRequest.getUsername());
        return new JwtResponse(jwtTokenUtils.generateToken(authRequest.getUsername()));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public SubjectHubUserResponse register(
        @RequestBody RegisterRequest registerRequest
    ) {
        // TODO: Handle usernames as case insensitive
        // I.e. usernames are still displayed with case sensitivity but are treated as case
        // insensitive.
        validateRegisterRequest(registerRequest);
        Application.log.info("Received register request with: {}", registerRequest);
        String hashedPassword = bCryptPasswordEncoder.encode(registerRequest.getPassword());
        SubjectHubUser subjectHubUser = new SubjectHubUser(registerRequest.getUsername(),
            hashedPassword, registerRequest.getEmail());
        subjectHubUserRepository.save(subjectHubUser);
        Application.log.info("{} successfully registered.", registerRequest.getUsername());
        return userResponse(subjectHubUser);
    }

    /**
     * Returns details of the user.
     *
     * Might remove this if not needed.
     */
    @RequestMapping(value = "/self", method = RequestMethod.GET)
    public SubjectHubUserResponse getUser() {
        UserDetails userDetails;
        try {
            userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException|ClassCastException e) {
            throw new SubjectHubException("Not logged in.");
        }

        Optional<SubjectHubUser> user = subjectHubUserRepository.findByUsername(
            userDetails.getUsername());

        if (!user.isPresent()) {
            throw new SubjectHubUnexpectedException(String.format("User %s is authenticated but " +
                "was not found in database", userDetails.getUsername()));
        }
        return userResponse(user.get());
    }

    /**
     * Validates register request by checking email and username fields.
     * @param registerRequest
     */
    private void validateRegisterRequest(RegisterRequest registerRequest) {
        if (!EmailValidator.getInstance().isValid(registerRequest.getEmail())) {
            throw new SubjectHubException("Invalid email address.");
        }

        if (subjectHubUserRepository.existsByUsername(registerRequest.getUsername())) {
            throw new SubjectHubException("Username already exists.");
        }

        if (subjectHubUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new SubjectHubException("Email already exists.");
        }

        if (!usernamePattern.matcher(registerRequest.getUsername()).matches()) {
            throw new SubjectHubException(
                "Username is invalid. Must start with letters and be between 5 and 20 characters.");
        }
    }

    /**
     * Converts a SubjectHubUser to a SubjectHubUserResponse object for return to a user.
     */
    private SubjectHubUserResponse userResponse(SubjectHubUser user) {
        SubjectHubUserResponse response = new SubjectHubUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
