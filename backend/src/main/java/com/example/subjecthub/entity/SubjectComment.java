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
import java.util.Date;

@Entity
@Table(name = "comments")
@ParametersAreNonnullByDefault
public class SubjectComment {

    @Id
    @GeneratedValue
    @Column(nullable = false, name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private SubjectHubUser user;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(nullable = false)
    private String post;

    @Column(nullable = false)
    private Date postTime;

    @Column(nullable = false)
    private Boolean isFlagged; //default false in schema

    @Column(nullable = false)
    private int thumbsUp; //default 0 in schema

    @Column(nullable = false)
    private int thumbsDown; //default 0 in schema

    public SubjectComment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubjectHubUser getUser() {
        return user;
    }

    public void setUser(SubjectHubUser user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    //set postTime to current time, TIMESTAMP datatype supports java.util.Date
    public void setPostTimeNow(){
        this.postTime = new Date();
    }

    public Boolean getFlagged() {
        return isFlagged;
    }

    public void setFlagged(Boolean flagged) {
        isFlagged = flagged;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    @Override
    public String toString() {
        return "SubjectComment{" +
            "id=" + id +
            ", user=" + user.getUsername() +
            ", subject=" + subject.getName() +
            ", postTime=" + postTime +
            ", isFlagged=" + isFlagged +
            '}';
    }
}
