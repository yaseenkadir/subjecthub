package com.example.subjecthub.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
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
