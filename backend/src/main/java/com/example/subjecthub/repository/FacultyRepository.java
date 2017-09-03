package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Faculty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FacultyRepository extends CrudRepository<Faculty, Long>{
    // Haven't verified if these work!
    List<Faculty> findByUniversityId(Long universityId);
}
