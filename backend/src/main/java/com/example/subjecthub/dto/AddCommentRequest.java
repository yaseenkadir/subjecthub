package com.example.subjecthub.dto;

/**
 * DTO sent by user when adding a comment.
 */
public class AddCommentRequest {
    private String comment;

    public AddCommentRequest() {
    }

    public AddCommentRequest(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
