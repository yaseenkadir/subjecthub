package com.example.subjecthub.api;

import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/universities/university/{universityId}/subjects")
@CrossOrigin(origins = "*")
public interface SubjectServiceApi {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Subject> getSubjects(
        @PathVariable Long universityId,
        @RequestParam(required = false) String subjectCode,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long facultyId,
        @RequestParam(required = false) String facultyName,
        @RequestParam(required = false) Double ratingStart,
        @RequestParam(required = false) Double ratingEnd,
        @RequestParam(required = false) Integer creditPoints,
        @RequestParam(required = false) String instructor
    );

    @RequestMapping(value = "/subject/{subjectId}/add-tag", method = RequestMethod.POST)
    public Subject addTagToSubject(@PathVariable Long subjectId, @RequestBody Tag tag);

    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.GET)
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    );
}
