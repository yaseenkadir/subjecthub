package com.example.subjecthub.api;

import com.example.subjecthub.entity.Assessment;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/universities/university/{universityId}/subjects/subject/{subjectId}/assessments")
@CrossOrigin(origins = "http://localhost:4200")
public interface AssessmentServiceApi {

    // get all assessments under a subject related to a university, with path parameters of universityId & subjectId
    // assessment type and/or weighting and/or group work can be used as a filter to find the specific assessment
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Assessment> getAssessments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) Integer weighting,
        @RequestParam(required = false) Boolean groupWork
    );

    //get a single assessment with assessmentId
    @RequestMapping(value = "/assessment/{assessmentId}", method = RequestMethod.GET)
    public Assessment getAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long assessmentId
    );
}
