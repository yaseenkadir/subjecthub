package com.example.subjecthub.api;

import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.entity.Tag;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface SubjectServiceApi {

    List<Subject> getSubjects(
        Long universityId,
        @Nullable String subjectCode,
        @Nullable String name,
        @Nullable Long facultyId,
        @Nullable String facultyName,
        @Nullable Double ratingStart,
        @Nullable Double ratingEnd,
        @Nullable Integer creditPoints,
        @Nullable String instructor
    );

    Subject addTagToSubject(Long universityId, Long subjectId, Tag tag);

    Subject getSubject(Long universityId, Long subjectId);

    void deleteSubject(Long universityId, Long subjectId);

    Subject editSubject(Long universityId, Long subjectId, Subject subject);

    Subject createSubject(Long universityId, Subject subject);
}
