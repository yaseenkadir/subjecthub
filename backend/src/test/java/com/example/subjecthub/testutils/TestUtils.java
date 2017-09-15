package com.example.subjecthub.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TestUtils {

    public static String asJson(Object o) throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer();
        return writer.writeValueAsString(o);
    }
}
