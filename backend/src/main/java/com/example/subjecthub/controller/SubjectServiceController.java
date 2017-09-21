package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.Tag;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.TagRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "*")
public class SubjectServiceController implements SubjectServiceApi {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Subject addTagToSubject(@PathVariable Long universityId,  @PathVariable Long subjectId, @RequestBody Tag tag) {

        Subject currentSubject = subjectRepository.findOne(subjectId);

        Tag existingTag = tagRepository.findByName(tag.getName());

        if (existingTag != null) {

            // if the subject is included on the tag then throw an error
            if (existingTag.getSubjects().contains(currentSubject)) {
                throw new IllegalArgumentException();
            }

            existingTag.getSubjects().add(currentSubject);
            tagRepository.save(existingTag);

            currentSubject.getTags().add(existingTag);
            subjectRepository.save(currentSubject);

            return currentSubject;
        }

        tag.getSubjects().add(currentSubject);
        currentSubject.getTags().add(tag);

        subjectRepository.save(currentSubject);
        return currentSubject;

    }

    @Override
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
    ) {
        // TODO: Move subjectCode retrieval to it's own method.
        // It is unique within a uni. We shouldn't return a list.
        Application.log.info("Received:\nsubjectCode {}\nfacultyId: {}\nratingStart: {}\n" +
                "ratingEnd: {}\nuniversityId: {}", subjectCode, facultyId, ratingStart, ratingEnd,
            universityId
        );

        // If subjectCode is supplied we don't check other GET params
        if (subjectCode != null) {
            return subjectRepository.findByCodeContainingIgnoreCase(subjectCode);
        }

        // If any of the params are not null we filter by their criteria.
        return subjectRepository.findByFaculty_University_Id(universityId).stream()
            .filter(s -> (facultyId == null || s.getFaculty().getId().equals(facultyId)))
            .filter(s -> (facultyName == null ||
                    FuzzyUtils.isSimilar(s.getFaculty().getName(), facultyName)))
            .filter(s -> (name == null || FuzzyUtils.isSimilar(s.getName(), name)))
            .filter(s -> (creditPoints == null || s.getCreditPoints() == creditPoints))
            .filter(s -> (ratingStart == null || s.getRating() >= ratingStart))
            .filter(s -> (ratingEnd == null) || s.getRating() <= ratingEnd)
            .collect(Collectors.toList());
    }

    @Override
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ) {
        // TODO: Move annotations from Service interface to implementation
        // TODO: Return null or throw exception if no subject is found
        // TODO: Don't allow cross university fetching
        return subjectRepository.findOne(subjectId);
    }
}
