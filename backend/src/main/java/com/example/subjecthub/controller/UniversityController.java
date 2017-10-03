package com.example.subjecthub.controller;

import com.example.subjecthub.api.UniversityServiceApi;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.UniversityRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "http://localhost:4200")
public class UniversityController implements UniversityServiceApi {

    @Autowired
    private UniversityRepository universityRepository;

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<University> getUniversities(
        @RequestParam(required = false) String abbreviation,
        @RequestParam(required = false) String name
    ) {
        return universityRepository.findAll().stream()
            .filter(u -> abbreviation == null || FuzzyUtils.isSimilar(abbreviation, u.getAbbreviation()))
            .filter(u -> name == null || FuzzyUtils.isSimilar(name, u.getName()))
            .collect(Collectors.toList());
    }

    @Override
    @RequestMapping(value = "university/{universityId}", method = RequestMethod.GET)
    public University getUniversity(
        @PathVariable Long universityId
    ) {
        return universityRepository.findOne(universityId);
    }
}
