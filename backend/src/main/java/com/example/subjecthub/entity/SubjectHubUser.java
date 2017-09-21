package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@ParametersAreNonnullByDefault
public class SubjectHubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(name = "comment_id")
    @JsonIgnoreProperties(value = {"user"})
    private List<SubjectComment> comments;

    public SubjectHubUser() {

    }

    public SubjectHubUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SubjectComment> getComments() {
        return comments;
    }

    public void setComments(List<SubjectComment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "SubjectHubUser{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
