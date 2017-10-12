package com.example.subjecthub.api;

import com.example.subjecthub.dto.AuthenticationRequest;
import com.example.subjecthub.dto.JwtResponse;
import com.example.subjecthub.dto.RegisterRequest;
import com.example.subjecthub.entity.SubjectHubUser;

public interface AuthServiceApi {

    JwtResponse authenticate(AuthenticationRequest authRequest);

    JwtResponse register(RegisterRequest registerRequest);

    SubjectHubUser getRequestingUser();
}
