package com.example.subjecthub.utils;

public class SubjectHubUnexpectedException extends SubjectHubException {

    public SubjectHubUnexpectedException() {
        super();
    }

    public SubjectHubUnexpectedException(String message) {
        super(message);
    }

    public SubjectHubUnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
