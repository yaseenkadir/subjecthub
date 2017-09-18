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
import com.example.subjecthub.utils.FuzzyUtils;

@RestController
@ParametersAreNonnullByDefault
@CrossOrigin(origins = "http://localhost:4200")
public class AssessmentServiceController implements AssessmentServiceApi {

	@Autowired
	private AssessmentRepository assessmentRepository;

	@Override
	public List<Assessment> getAssessments(
			@PathVariable Long universityId,
			@PathVariable Long subjectId,
			@RequestParam(required = false) Integer assessmentType,
			@RequestParam(required = false) Integer weighting,
			@RequestParam(required = false) Boolean isGroupWork
			) {

		Application.log.info("Received:\n assessmentType: {}\n weighting: {}\n isGroupWork: {}\n universityId: {}\n subjectId: {}",
				assessmentType, weighting, isGroupWork, universityId, subjectId);

		return assessmentRepository.findBySubjectId(subjectId).stream()
				.filter(s -> (assessmentType == null|| s.getType() == assessmentType))
				.filter(s -> (weighting == null || s.getWeighting() == weighting))
				.filter(s -> (isGroupWork == null || s.isGroupWork() == isGroupWork))
				.collect(Collectors.toList());
	}

	@Override
	public Assessment getAssessment(
			@PathVariable Long universityId,
			@PathVariable Long subjectId,
			@PathVariable Long assessmentId
			) {
		return assessmentRepository.findOne(assessmentId);
	}


	/*private boolean compareAssessmentType(Integer assessmentType){
	    //ordinal() starts from 0, so that we have to minus 1
        int tempAssessmentType = assessmentType - 1;

		if(Assessment.AssessmentType.FINAL.ordinal() == tempAssessmentType ||
				Assessment.AssessmentType.PROJECT.ordinal() == tempAssessmentType ||
				Assessment.AssessmentType.REPORT.ordinal() == tempAssessmentType ||
				Assessment.AssessmentType.TEST.ordinal() == tempAssessmentType ){
			return true;
		}
		return false;
	}

	private boolean checkIsSimilar(int weighting, int tempWeighting){
		return FuzzyUtils.isSimilar(String.valueOf(weighting), String.valueOf(tempWeighting));
	}*/
}
