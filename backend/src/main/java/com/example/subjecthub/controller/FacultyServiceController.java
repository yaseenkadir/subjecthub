package com.example.subjecthub.controller;

import com.example.subjecthub.api.FacultyServiceApi;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.repository.FacultyRepository;
import com.example.subjecthub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value="/api/universities/university/{universityId}/faculties")
@RestController
@ParametersAreNonnullByDefault
public class FacultyServiceController implements FacultyServiceApi {

    @Autowired
    private FacultyRepository facultyRepository;

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
}
