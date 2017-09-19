package com.example.subjecthub.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
@ParametersAreNonnullByDefault
public class SubjectComment {

    // TODO: Add thumbs, Add flag, Fix Dates/Timestamp > need help

    @Id
    @GeneratedValue
    @Column(nullable = false, name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Long subject;

    @Column(nullable = false)
    private String post;

    public SubjectComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "SubjectComment{" +
            "id=" + id +
            ", user=" + user +
            ", subject=" + subject +
            ", post='" + post + '\'' +
            '}';
    }
}
