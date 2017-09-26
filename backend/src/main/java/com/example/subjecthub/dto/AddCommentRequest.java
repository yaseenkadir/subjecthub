package com.example.subjecthub.dto;

/**
 * DTO sent by user when adding a comment.
 */
public class AddCommentRequest {
    private Long userId;
    private String comment;

    public AddCommentRequest() {
    }

    public AddCommentRequest(Long userId, String comment) {
        this.userId = userId;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
