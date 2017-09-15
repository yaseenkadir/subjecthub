package com.example.subjecthub.dto;

/**
 * DTO sent from user containing user details.
 *
 * Does not include password as that is not needed by user.
 */
public class SubjectHubUserResponse {

    private String username;
    private String email;

    public SubjectHubUserResponse() {
    }

    public SubjectHubUserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
