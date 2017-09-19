package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Assessment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssessmentRepository extends CrudRepository<Assessment, Long> {
    List<Assessment> findBySubjectId(Long subjectId);
}
