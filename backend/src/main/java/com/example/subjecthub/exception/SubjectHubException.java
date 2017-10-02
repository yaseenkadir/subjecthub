package com.example.subjecthub.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when client request cannot be processed or is invalid. For server errors see
 * {@link SubjectHubUnexpectedException}.
 *
 * This exception is caught by
 * {@link ExceptionAdvice#handleSubjectHubException(SubjectHubException)} and the provided message
 * and http code are returned to the user.
 */
public class SubjectHubException extends RuntimeException {

    private HttpStatus status;

    /**
     * Creates new exception with HttpStatus 400 - Bad Request.
     * @param message user friendly error message
     */
    public SubjectHubException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Exception thrown due to bad client request.
     * @param status HttpStatus to return to user.
     * @param message user friendly error message
     */
    public SubjectHubException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Convenience method to create 404 Exception.
     * @param message user friendly error message
     */
    public static SubjectHubException notFound(String message) {
        return new SubjectHubException(HttpStatus.NOT_FOUND, message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
