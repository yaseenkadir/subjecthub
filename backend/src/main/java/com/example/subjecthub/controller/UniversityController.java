package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.UniversityServiceApi;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import com.example.subjecthub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "*")
@RequestMapping("/api/universities")
public class UniversityController implements UniversityServiceApi {

    @Autowired
    private UniversityRepository universityRepository;

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<University> getUniversities(
        @RequestParam(required = false) @Nullable String abbreviation,
        @RequestParam(required = false) @Nullable String name
    ) {
        return universityRepository.findAll().stream()
            .filter(u -> abbreviation == null || FuzzyUtils.isSimilar(abbreviation, u.getAbbreviation()))
            .filter(u -> name == null || FuzzyUtils.isSimilar(name, u.getName()))
            .collect(Collectors.toList());
    }

    @Override
    @RequestMapping(value = "/university/{universityId}", method = RequestMethod.GET)
    public University getUniversity(
        @PathVariable Long universityId
    ) {
        University u = universityRepository.findOne(universityId);
        Utils.ifNull404(u, "University not found.");
        return u;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value ="/university/{universityId}", method = RequestMethod.DELETE)
    public void deleteUniversity(
        @PathVariable Long universityId
    ) {
        University u = getUniversity(universityId);
        Application.log.info("Deleting {}.", u);
        universityRepository.delete(u);
    }

    @Override
    @RequestMapping(value = "/university/{universityId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public University editUniversity(
        @PathVariable Long universityId,
        @RequestBody University university
    ) {
        // TODO: Add proper validation on fields
        if (university.getId() != null && !universityId.equals(university.getId())) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Payload universityId and URL path universityId do not match.");
        }
        university.setId(universityId);
        return universityRepository.save(university);
    }

    @Override
    @RequestMapping(value = "/university", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public University createUniversity(
        @RequestBody University university
    ) {
        if (university.getId() != null) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Cannot specify id for university creation.");
        }
        return universityRepository.save(university);
    }
}
