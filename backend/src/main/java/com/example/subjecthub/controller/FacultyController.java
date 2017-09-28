package com.example.subjecthub.controller;

import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value="/api/universities/university/{universityId}/faculties")
@RestController
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepository;

    private List<Faculty> findByCode(String code) {
        return facultyRepository.findByCode(code);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Faculty> getFaculties(
        @PathVariable Long universityId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String code
    ) {
        return facultyRepository.findByUniversityId(universityId).stream()
            .filter(s -> (name == null||s.getName().equalsIgnoreCase(name)))
            .filter(s -> (code == null||s.getCode().equalsIgnoreCase(code)))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/faculty/{facultyId}", method = RequestMethod.GET)
    private Faculty getFaculty(
        @PathVariable Long universityId,
        @PathVariable Long facultyId
    ) {
        return facultyRepository.findOne(facultyId);
    }
}
