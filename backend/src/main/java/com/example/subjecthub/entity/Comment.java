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
public class Comment {

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

    //@Column(nullable = false)
    //private DateTime postTime;

    public Comment() {
    }

    public Comment(Long user, Long subject, String post) {
        this.user = user;
        this.subject = subject;
        this.post = post;
        //this.postTime = currentdatetime();
    }
/*
    public Comment(Long user, Long subject, String post, DateTime postTime){
        this.user = user;
        this.subject = subject;
        this.post = post;
        this.postTime = postTime;
    }
*/
    public Comment(SubjectHubUser user, Subject subject, String post){
        this.user = user.getId();
        this.subject = subject.getId();
        this.post = post;
        //this.posttime = current datetime();
    }
/*
    public Comment(SubjectHubUser user, Subject subject, String post, DateTime postTime){
        this.user = user.getId();
        this.subject = subject.getId();
        this.post = post;
        this.postTime = postTime;
    }
*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /*
    public DateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(DateTime postTime) {
        this.postTime = postTime;
    }
    */

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", user=" + user +
            ", subject=" + subject +
            ", post='" + post + '\'' +
            //", postTime=" + postTime +
            '}';
    }
}
