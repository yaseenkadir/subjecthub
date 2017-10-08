package com.example.subjecthub.utils;

import com.example.subjecthub.exception.SubjectHubException;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;

public class Utils {

    public static void ifNull404(@Nullable Object o, String message) {
        if (o == null) {
            throw new SubjectHubException(HttpStatus.NOT_FOUND, message);
        }
    }
}
