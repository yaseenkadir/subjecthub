package com.example.subjecthub.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown in cases that backend could not reasonably recover from. Should only be used for 5xx
 * server errors. Client errors should instead throw {@link SubjectHubException}.
 *
 * This error is caught by
 * {@link ExceptionAdvice#handleSubjectHubUnexpectedException(SubjectHubUnexpectedException)} and
 * returns the supplied error message to the user.
 */
public class SubjectHubUnexpectedException extends SubjectHubException {

    public SubjectHubUnexpectedException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public SubjectHubUnexpectedException(HttpStatus status, String message) {
        super(status, message);
    }
}
