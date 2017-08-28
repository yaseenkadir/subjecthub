package com.example.subjecthub.api;

import com.example.subjecthub.entity.Subject;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/subjects")
public interface SubjectServiceApi {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Subject> getSubjects(
        @RequestParam(required = false) String subjectCode,
        @RequestParam(required = false) Long facultyId,
        @RequestParam(required = false) Double ratingStart,
        @RequestParam(required = false) Double ratingEnd,
        @RequestParam(required = false) String instructor // TODO: Use instructor
    );

    @RequestMapping(value = "/subject/{id}", method = RequestMethod.GET)
    public Subject getSubject(
        @PathVariable Long id
    );
}
