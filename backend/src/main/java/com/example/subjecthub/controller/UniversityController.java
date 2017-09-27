package com.example.subjecthub.controller;

import com.example.subjecthub.api.UniversityServiceApi;
import com.example.subjecthub.entity.University;
import com.example.subjecthub.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
        if (abbreviation != null && name != null)
            return universityRepository.findByAbbreviationLikeIgnoreCaseOrNameLikeIgnoreCase(abbreviation, name);
        return universityRepository.findAll();
    }

    @Override
    @RequestMapping(value = "university/{universityId}", method = RequestMethod.GET)
    public University getUniversity(
        @PathVariable Long universityId
    ) {
        return universityRepository.findOne(universityId);
    }
}
