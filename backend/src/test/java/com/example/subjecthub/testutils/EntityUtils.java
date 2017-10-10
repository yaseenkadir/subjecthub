package com.example.subjecthub.testutils;

import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.entity.Faculty;
import com.example.subjecthub.entity.Subject;
import com.example.subjecthub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityUtils {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private SubjectCommentRepository subjectCommentRepository;

    /**
     * Util test method that handles extraneous params for creating subject objects.
     */
    public Subject createSubject(String name, String code, Faculty faculty) {
        Subject testSubject = new Subject();
        testSubject.setName(name);
        testSubject.setCode(code);
        testSubject.setDescription("desc");
        testSubject.setFaculty(faculty);
        testSubject.setSummer(true);
        testSubject.setSpring(false);
        testSubject.setAutumn(true);
        testSubject.setMinRequirements("min requirements");
        testSubject.setRating(1);
        testSubject.setNumRatings(1);
        testSubject.setCreditPoints(1);
        testSubject.setUndergrad(true);
        testSubject.setPostgrad(true);
        return subjectRepository.save(testSubject);
    }

    public Assessment createAssessment(String name, Subject subject) {
        Assessment assessment = new Assessment();
        assessment.setName(name);
        assessment.setDescription("A report");
        assessment.setGroupWork(true);
        assessment.setLength("2000 words");
        assessment.setSubject(subject);
        assessment.setType(Assessment.AssessmentType.REPORT);
        assessment.setWeighting(50);
        return assessmentRepository.save(assessment);
    }
}
