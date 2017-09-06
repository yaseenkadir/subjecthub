package com.example.subjecthub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

@RequestMapping("/")
@RestController
public class RootController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map<String, Serializable> getIndex() {
        return Collections.singletonMap("Status", "OK");
    }
}
