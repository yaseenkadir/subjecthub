package com.example.subjecthub.api;

import com.example.subjecthub.entity.Faculty;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface FacultyServiceApi {

    List<Faculty> getFaculties(Long universityId, @Nullable String name, @Nullable String code);

    Faculty getFaculty(Long universityId, Long facultyId);

    void deleteFaculty(Long universityId, Long facultyId);

    Faculty editFaculty(Long universityId, Long facultyId, Faculty faculty);

    Faculty createFaculty(Long universityId, Faculty faculty);
}
