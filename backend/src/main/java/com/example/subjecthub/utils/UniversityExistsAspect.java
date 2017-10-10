package com.example.subjecthub.utils;

import com.example.subjecthub.Application;
import com.example.subjecthub.exception.SubjectHubException;
import com.example.subjecthub.repository.SubjectRepository;
import com.example.subjecthub.repository.UniversityRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Configuration
public class UniversityExistsAspect {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Before(
        "execution(* com.example.subjecthub.controller.SubjectServiceController.getSubjects (..)) || " +
        "execution(* com.example.subjecthub.controller.SubjectServiceController.getSubject (..)) || " +
        "execution(* com.example.subjecthub.controller.FacultyServiceController.getFaculties (..)) || "+
        "execution(* com.example.subjecthub.controller.FacultyServiceController.getFaculty (..)) || "+
        "execution(* com.example.subjecthub.controller.FacultyServiceController.createFaculty (..)) || "+
        "execution(* com.example.subjecthub.controller.FacultyServiceController.editFaculty (..)) || "+
        "execution(* com.example.subjecthub.controller.AssessmentServiceController.getAssessment (..)) || " +
        "execution(* com.example.subjecthub.controller.AssessmentServiceController.getAssessments (..)) || " +
        "execution(* com.example.subjecthub.controller.CommentsServiceController.getComments (..)) || " +
        "execution(* com.example.subjecthub.controller.CommentsServiceController.getComment (..)) || " +
        "execution(* com.example.subjecthub.controller.UniversityController.getUniversity (..)) || " +
        "execution(* com.example.subjecthub.controller.UniversityController.deleteUniversity (..)) || " +
        "execution(* com.example.subjecthub.controller.UniversityController.editUniversity (..))"
    )
    public boolean universityExists(JoinPoint joinPoint) {
        Application.log.info("Checking if university exists.");
        Long universityId = (Long) joinPoint.getArgs()[0];
        if (!universityRepository.exists(universityId)) {
            Application.log.info("Call to {} dropped because University was not found.",
                joinPoint.getSignature());
            throw new SubjectHubException(HttpStatus.NOT_FOUND, "University not found.");
        }
        return false;
    }

    @Before(
            "execution(* com.example.subjecthub.controller.AssessmentServiceController.getAssessment* (..)) || " +
            "execution(* com.example.subjecthub.controller.CommentsServiceController.getComment* (..))")
    public boolean subjectExists(JoinPoint joinPoint) {
        Application.log.info("Checking if subject exists.");
        Long subjectId = (Long) joinPoint.getArgs()[1];
        if (!subjectRepository.exists(subjectId)) {
            Application.log.info("Call to {} dropped because Subject was not found.",
                joinPoint.getSignature());
            throw new SubjectHubException(HttpStatus.NOT_FOUND, "Subject not found.");
        }
        return false;
    }
}
