package com.example.subjecthub.utils;

import com.example.subjecthub.Application;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SubjectHubException.class)
    public ExceptionResponse handleSubjectHubException(SubjectHubException she) {
        Application.log.info(she.getMessage());
        return ExceptionResponse.badRequest(she.getMessage());
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
