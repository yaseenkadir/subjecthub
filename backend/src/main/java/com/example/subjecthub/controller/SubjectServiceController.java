package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.repository.SubjectRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectServiceController implements SubjectServiceApi {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getSubjects(String subjectCode, Long facultyId, Double ratingStart,
        Double ratingEnd, String instructor) {

        // TODO: Move subjectCode retrieval to it's own method.
        // It is unique within a uni. We shouldn't return a list.
        Application.log
            .info("Received:\nsubjectCode {}\nfacultyId: {}\nratingStart: {}\n ratingEnd: {}",
                subjectCode, facultyId, ratingStart, ratingEnd);
        // If subjectCode is supplied we don't check other GET params
        if (subjectCode != null) {
            return subjectRepository.findAllByCode(subjectCode);
        }

        Stream<Subject> subjectStream = subjectRepository.findAll().stream();

        if (facultyId != null) {
            subjectStream = subjectStream.filter(s -> s.getFaculty().getId().equals(facultyId));
        }

        if (ratingStart != null || ratingEnd != null) {
            final Double start = (ratingStart == null ? new Double("0.0") : ratingStart);
            final Double end = (ratingEnd == null ? new Double("10.0") : ratingEnd);

            subjectStream = subjectStream
                .filter(s -> s.getRating() >= start)
                .filter(s -> s.getRating() <= end);
        }

        return subjectStream.collect(Collectors.toList());
    }

    @Override
    public Subject getSubject(Long id) {
        return subjectRepository.findOne(id);
    }
}
