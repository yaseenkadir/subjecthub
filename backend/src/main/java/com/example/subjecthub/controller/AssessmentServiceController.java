package com.example.subjecthub.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.subjecthub.Application;
import com.example.subjecthub.api.AssessmentServiceApi;
import com.example.subjecthub.entity.Assessment;
import com.example.subjecthub.repository.AssessmentRepository;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "http://localhost:4200")
public class AssessmentServiceController implements AssessmentServiceApi {

	@Autowired
	private AssessmentRepository assessmentRepository;

	// business logic of get all assessments
	@Override
	public List<Assessment> getAssessments(
			@PathVariable Long universityId,
			@PathVariable Long subjectId,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false) Integer weighting,
			@RequestParam(required = false) Boolean groupWork
			) {

		Application.log.info("Received:\n type: {}\n weighting: {}\n groupWork: {}\n universityId: {}\n subjectId: {}",
            type, weighting, groupWork, universityId, subjectId);

		// filter to compare the type / weighting / group work in Assessment entity with the value passed in the request parameter
        // then return the assessment list match the criteria
		return assessmentRepository.findBySubjectId(subjectId).stream()
				.filter(s -> (type == null|| s.getType().ordinal() == type))
				.filter(s -> (weighting == null || s.getWeighting() == weighting))
				.filter(s -> (groupWork == null || s.isGroupWork() == groupWork))
				.collect(Collectors.toList());
	}

	// find a single assessment by its assessment id
	@Override
	public Assessment getAssessment(
			@PathVariable Long universityId,
			@PathVariable Long subjectId,
			@PathVariable Long assessmentId
			) {
	    //To avoid cross university & subject fetching later
		return assessmentRepository.findOne(assessmentId);
	}
}
