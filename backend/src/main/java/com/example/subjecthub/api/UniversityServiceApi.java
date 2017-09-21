package com.example.subjecthub.api;

import com.example.subjecthub.entity.University;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/universities")
public interface UniversityServiceApi {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<University> getUniversities(
        @RequestParam(required = false) String abbreviation
    );

    @RequestMapping(value = "university/{universityId}", method = RequestMethod.GET)
    public University getUniversity(
        @PathVariable Long universityId
    );
}
