package com.example.subjecthub.api;

import com.example.subjecthub.entity.University;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UniversityServiceApi {

    List<University> getUniversities(
        @Nullable String abbreviation,
        @Nullable String name
    );

    University getUniversity(
        Long universityId
    );

    void deleteUniversity(
        Long universityId
    );

    University editUniversity(
        Long universityId,
        University university
    );

    University createUniversity(
        University university
    );
}
