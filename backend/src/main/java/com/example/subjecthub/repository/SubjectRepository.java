package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    List<Subject> findByFaculty_University_Id(Long universityId);

    List<Subject> findByCodeContainingIgnoreCase(String code);
}
