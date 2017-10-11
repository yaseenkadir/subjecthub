package com.example.subjecthub.controller;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.AssessmentServiceApi;
import com.example.subjecthub.api.SubjectServiceApi;
import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.repository.AssessmentRepository;
import com.example.subjecthub.utils.FuzzyUtils;
import com.example.subjecthub.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "*")
@RequestMapping("/api/universities/university/{universityId}/subjects/subject/{subjectId}/assessments")
public class AssessmentServiceController implements AssessmentServiceApi {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private SubjectServiceApi subjectServiceApi;

    /**
     * Gets all assessments for a specific university and subject. If optional params are non null,
     * they will be used to filter results.
     * @param universityId
     * @param subjectId
     * @param type - Optional, filter by type
     * @param weighting - Optional, filter by weighting
     * @param groupWork - Optional, filter by whether it has groupWork or not
     * @param name - Optional, filter by name
     * @return
     */
    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Assessment> getAssessments(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestParam(required = false) @Nullable Assessment.AssessmentType type,
        @RequestParam(required = false) @Nullable Integer weighting,
        @RequestParam(required = false) @Nullable Boolean groupWork,
        @RequestParam(required = false) @Nullable String name
    ) {

        Application.log.info("Received:\n type: {}\n weighting: {}\n groupWork: {}\n name: {}\n universityId: {}\n subjectId: {}",
            type, weighting, groupWork, name, universityId, subjectId);

        // filter to compare the type / weighting / group work in Assessment entity with the value
        // passed in the request parameter then return the assessment list match the criteria
        return assessmentRepository.findBySubjectId(subjectId).stream()
            .filter(s -> (type == null || s.getType() == type))
            .filter(s -> (weighting == null || s.getWeighting() == weighting))
            .filter(s -> (groupWork == null || s.isGroupWork() == groupWork))
            .filter(s -> (name == null || FuzzyUtils.isSimilar(s.getName(), name)))
            .collect(Collectors.toList());
    }

    //

    /**
     * Find a single assessment by its assessment id
     * @param universityId
     * @param subjectId
     * @param assessmentId
     * @return
     */
    @Override
    @RequestMapping(value = "/assessment/{assessmentId}", method = RequestMethod.GET)
    @Nonnull
    public Assessment getAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long assessmentId
    ) {
        Assessment a = assessmentRepository.findOne(assessmentId);
        Utils.ifNull404(a, "Assessment not found.");
        return assessmentRepository.findOne(assessmentId);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/assessment/{assessmentId}", method = RequestMethod.DELETE)
    public void deleteAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long assessmentId
    ) {
        assessmentRepository.delete(getAssessment(universityId, subjectId, assessmentId));
    }

    @Override
    @RequestMapping(value = "/assessment/{assessmentId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Assessment editAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @PathVariable Long assessmentId,
        @RequestBody Assessment assessment
    ) {
        if (assessment.getId() != null && !assessmentId.equals(assessment.getId())) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Payload assessmentId and URL path assessmentId do not match.");
        }

        Subject subject = subjectServiceApi.getSubject(universityId, subjectId);
        assessment.setSubject(subject);
        assessment.setId(assessmentId);
        return assessmentRepository.save(assessment);
    }

    @Override
    @RequestMapping(value = "/assessment", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Assessment createAssessment(
        @PathVariable Long universityId,
        @PathVariable Long subjectId,
        @RequestBody Assessment assessment
    ) {
        if (assessment.getId() != null) {
            throw new SubjectHubException(HttpStatus.BAD_REQUEST,
                "Cannot specify id for assessment creation.");
        }
        Subject subject = subjectServiceApi.getSubject(universityId, subjectId);
        assessment.setSubject(subject);
        return assessmentRepository.save(assessment);
    }
}
