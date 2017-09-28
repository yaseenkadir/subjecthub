package com.example.subjecthub.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="That tag already exists")
public class TagAlreadyExistsOnSubjectException extends RuntimeException{
}
