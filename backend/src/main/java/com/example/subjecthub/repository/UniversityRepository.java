package com.example.subjecthub.repository;

import com.example.subjecthub.entity.University;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UniversityRepository extends CrudRepository<University, Long> {

    List<University> findAll();

    boolean exists(Long universityId);
}
