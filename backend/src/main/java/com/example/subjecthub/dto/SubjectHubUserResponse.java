package com.example.subjecthub.dto;

/**
 * DTO sent from user containing user details.
 *
 * Does not include password as that is not needed by user.
 */
public class SubjectHubUserResponse {

    private String username;
    private String email;
    private Boolean admin;

    public SubjectHubUserResponse() {
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
