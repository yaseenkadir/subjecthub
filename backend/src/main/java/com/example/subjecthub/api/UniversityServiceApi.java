package com.example.subjecthub.api;

import com.example.subjecthub.entity.University;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UniversityServiceApi {

    public List<University> getUniversities(
        @Nullable String abbreviation,
        @Nullable String name
    );

    public University getUniversity(
        Long universityId
    );
}
