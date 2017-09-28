package com.example.subjecthub.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such Subject")
public class SubjectNotFoundException extends RuntimeException{
}


