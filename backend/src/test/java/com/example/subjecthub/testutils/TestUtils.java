package com.example.subjecthub.testutils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class TestUtils {

    public static String asJson(Object o) throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer();
        return writer.writeValueAsString(o);
    }

    public static JsonNode fromString(String s) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(s);
    }
}
