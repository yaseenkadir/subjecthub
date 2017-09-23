package com.example.subjecthub.utils;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private int status;
    private String message;

    public ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ExceptionResponse serverError(String message) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public static ExceptionResponse badRequest(String message) {
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static ExceptionResponse notFound(String message) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), message);
    }

    public static ExceptionResponse unauthorized(String message) {
        return new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), message);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
