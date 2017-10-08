package com.example.subjecthub.exception;

import com.example.subjecthub.Application;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.ParametersAreNonnullByDefault;

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

    @Order(Integer.MAX_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleException(Exception e) {
        Application.log.error("Unexpected exception", e);
        return ExceptionResponse.serverError("Unexpected server error.");
    }
}
