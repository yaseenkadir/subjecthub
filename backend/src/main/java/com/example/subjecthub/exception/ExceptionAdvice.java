package com.example.subjecthub.exception;

import com.example.subjecthub.Application;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ControllerAdvice
@ParametersAreNonnullByDefault
public class ExceptionAdvice {

    @Order(0)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SubjectHubUnexpectedException.class)
    public ExceptionResponse handleSubjectHubUnexpectedException(
        SubjectHubUnexpectedException shue
    ) {
        return handleException(shue);
    }

    @ResponseBody
    @Order(1)
    @ExceptionHandler(SubjectHubException.class)
    public ResponseEntity<ExceptionResponse> handleSubjectHubException(SubjectHubException she) {
        Application.log.info(she.getMessage());

        return ResponseEntity
            .status(she.getStatus())
            .body(new ExceptionResponse(she.getStatus(), she.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ade) {
        SubjectHubException she = new SubjectHubException(HttpStatus.UNAUTHORIZED, ade.getMessage());
        return handleSubjectHubException(she);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException hmnre) {
        Application.log.info(hmnre.getMessage());
        SubjectHubException she = new SubjectHubException(HttpStatus.BAD_REQUEST,
            "Invalid request body.");
        return handleSubjectHubException(she);
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException hrmnse) {
        SubjectHubException she = new SubjectHubException(HttpStatus.METHOD_NOT_ALLOWED,
            hrmnse.getMethod() + " is not supported for that endpoint.");
        return handleSubjectHubException(she);
    }

    @Order(Integer.MAX_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException(Exception e) {
        Application.log.error("Unexpected exception", e);
        return ExceptionResponse.serverError("Unexpected server error.");
    }
}
