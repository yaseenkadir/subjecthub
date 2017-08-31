package com.example.subjecthub.api;

import com.example.subjecthub.entity.University;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/universities")
public interface UniversityServiceApi {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<University> getUniversities(@RequestParam(required = false) String abbreviation);

    @RequestMapping(value = "university/{id}", method = RequestMethod.GET)
    public University getUniversity();
}
