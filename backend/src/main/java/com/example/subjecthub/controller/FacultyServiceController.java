package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.FacultyServiceApi;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.repository.UniversityRepository;
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

@RequestMapping(value="/api/universities/university/{universityId}/faculties")
@CrossOrigin(origins ="*")
@RestController
@ParametersAreNonnullByDefault
public class FacultyServiceController implements FacultyServiceApi {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Faculty> getFaculties(
        @PathVariable Long universityId,
        @RequestParam(required = false) @Nullable String name,
        @RequestParam(required = false) @Nullable String code
    ) {
        return facultyRepository.findByUniversityId(universityId).stream()
            .filter(s -> (name == null||s.getName().equalsIgnoreCase(name)))
            .filter(s -> (code == null||code.equalsIgnoreCase(s.getCode())))
            .collect(Collectors.toList());
    }

    @Override
    @Nonnull
    @RequestMapping(value = "/faculty/{facultyId}", method = RequestMethod.GET)
    public Faculty getFaculty(
        @PathVariable Long universityId,
        @PathVariable Long facultyId
    ) {
        Faculty f = facultyRepository.findOne(facultyId);
        Utils.ifNull404(f, "Faculty not found.");
        return f;
    }

    @Override
    @RequestMapping(value = "/faculty/{facultyId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteFaculty(
        @PathVariable Long universityId,
        @PathVariable Long facultyId
    ) {
        Faculty f = getFaculty(universityId, facultyId);
        Application.log.info("Deleting {}.", f);
        facultyRepository.delete(f.getId());
    }

    @Override
    @RequestMapping(value = "/faculty/{facultyId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Faculty editFaculty(
        @PathVariable Long universityId,
        @PathVariable Long facultyId,
        @RequestBody Faculty faculty
    ) {
        // TODO: Add proper validation on fields
        if (faculty.getId() != null && !facultyId.equals(faculty.getId())) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Payload facultyId and URL path facultyId do not match.");
        }
        University university = universityRepository.findOne(universityId);
        faculty.setUniversity(university);
        return facultyRepository.save(faculty);
    }

    @Override
    @RequestMapping(value = "/faculty", method = RequestMethod.POST)
    public Faculty createFaculty(
        @PathVariable Long universityId,
        @RequestBody Faculty faculty
    ) {
        if (faculty.getId() != null) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Cannot specify id for faculty creation.");
        }
        faculty.setUniversity(universityRepository.findOne(universityId));
        return facultyRepository.save(faculty);
    }
}
