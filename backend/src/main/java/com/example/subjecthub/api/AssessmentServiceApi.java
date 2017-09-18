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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Assessment> getAssessments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestParam(required = false) Integer assessmentType,
        @RequestParam(required = false) Integer weighting,
        @RequestParam(required = false) Boolean isGroupWork
    );

    @RequestMapping(value = "/assessment/{assessmentId}", method = RequestMethod.GET)
    public Assessment getAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long assessmentId
    );
}
