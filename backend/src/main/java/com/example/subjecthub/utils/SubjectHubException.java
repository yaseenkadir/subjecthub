package com.example.subjecthub.utils;

public class SubjectHubException extends RuntimeException {

    public SubjectHubException() {
        super();
    }

    public SubjectHubException(String message) {
        super(message);
    }

    public SubjectHubException(String message, Throwable cause) {
        super(message, cause);
    }
}
