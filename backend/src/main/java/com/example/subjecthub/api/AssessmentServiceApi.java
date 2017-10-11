package com.example.subjecthub.api;

import com.example.subjecthub.entity.Assessment;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface AssessmentServiceApi {

    List<Assessment> getAssessments(
        Long universityId,
        Long subjectId,
        @Nullable Assessment.AssessmentType type,
        @Nullable Integer weighting,
        @Nullable Boolean groupWork,
        @Nullable String name
    );

    Assessment getAssessment(Long universityId, Long subjectId, Long assessmentId);

    void deleteAssessment(Long universityId, Long subjectId, Long assessmentId);

    Assessment editAssessment(
        Long universityId,
        Long subjectId,
        Long assessmentId,
        Assessment assessment
    );

    Assessment createAssessment(Long universityId, Long subjectId, Assessment assessment);
}
