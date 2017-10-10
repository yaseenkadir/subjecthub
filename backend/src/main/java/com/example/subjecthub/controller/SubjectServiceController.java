package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.Tag;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.TagRepository;
import com.example.subjecthub.utils.FuzzyUtils;

import com.example.subjecthub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "*")
@RequestMapping("/api/universities/university/{universityId}/subjects")
public class SubjectServiceController implements SubjectServiceApi {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Override
    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/subject/{subjectId}/addTag", method = RequestMethod.POST)
    public Subject addTagToSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody Tag tag
    ) {

        // TODO: call getSubject() here instead of manually null checking
        Subject currentSubject = getSubject(universityId, subjectId);

        Tag existingTag = tagRepository.findByName(tag.getName());

        if (existingTag != null) {
            // if the subject is included on the tag then throw an error
            if (existingTag.getSubjects().contains(currentSubject)) {
                throw new SubjectHubException("Tag already exists for subject.");
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
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Subject> getSubjects(
        @PathVariable Long universityId,
        @RequestParam(required = false) @Nullable String subjectCode,
        @RequestParam(required = false) @Nullable String name,
        @RequestParam(required = false) @Nullable Long facultyId,
        @RequestParam(required = false) @Nullable String facultyName,
        @RequestParam(required = false) @Nullable Double ratingStart,
        @RequestParam(required = false) @Nullable Double ratingEnd,
        @RequestParam(required = false) @Nullable Integer creditPoints,
        @RequestParam(required = false) @Nullable String instructor
    ) {
        // TODO: Move subjectCode retrieval to it's own method.
        // It is unique within a uni. We shouldn't return a list.
        Application.log.info("Received:\nsubjectCode {}\nfacultyId: {}\nratingStart: {}\n" +
                "ratingEnd: {}\nuniversityId: {}", subjectCode, facultyId, ratingStart, ratingEnd,
            universityId
        );

        // TODO: Revisit this behaviour. Is this what we want?
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
    @Nonnull
    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.GET)
    public Subject getSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ) {
        // TODO: Move annotations from Service interface to implementation
        Subject s = subjectRepository.findOne(subjectId);
        Utils.ifNull404(s, "Subject not found.");
        return s;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.DELETE)
    public void deleteSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId
    ) {
        // getSubject handles not found exception.
        Subject s = getSubject(universityId, subjectId);
        subjectRepository.delete(s);
    }

    @Override
    @RequestMapping(value = "/subject/{subjectId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Subject editSubject(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody Subject subject
    ) {
        if (subject.getId() != null && !subjectId.equals(subject.getId())) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Payload subjectId and URL path subjectId do not match.");
        }

        validateSubjectPayload(subject);

        Faculty faculty = facultyRepository.findOne(subject.getFaculty().getId());
        Utils.ifNull404(faculty, "Faculty not found.");
        subject.setId(subjectId);
        subject.setFaculty(faculty);
        Subject existing = subjectRepository.findOne(subjectId);

        subject.setAssessments(existing.getAssessments());
        subject.setTags(existing.getTags());
        subject.setComments(existing.getComments());
        return subjectRepository.save(subject);
    }

    @Override
    @RequestMapping(value = "/subject", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Subject createSubject(
        @PathVariable Long universityId,
        @RequestBody Subject subject
    ) {
        if (subject.getId() != null) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Cannot specify id for subject creation.");
        }
        validateSubjectPayload(subject);
        Faculty f = facultyRepository.findOne(subject.getFaculty().getId());
        subject.setFaculty(f);
        return subjectRepository.save(subject);
    }

    private void validateSubjectPayload(Subject subject) {

        // TODO: Adjust API bulk return
        // We can't return everything in a subject response. They are monstrous and require hacks
        // like this in order to manage everything as expected.
        String message = null;
        if (subject.getComments() != null && subject.getComments().size() != 0) {
            message = "Cannot specify comments in payload.";
        }

        if (subject.getAssessments() != null && subject.getAssessments().size() != 0) {
            message = "Cannot specify assessments in payload.";
        }

        if (subject.getTags() != null && subject.getTags().size() != 0) {
            message = "Cannot specify tags in payload.";
        }

        // This screams poor data design. We should figure out a simpler/better way.
        // TODO: Figure out a better solution to needing a faculty
        if (subject.getFaculty() == null || subject.getFaculty().getId() == null) {
            message = "Must include a nested faculty in the request with an associated facultyId.";
        }

        if (message != null) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST, message);
        }
    }
}
