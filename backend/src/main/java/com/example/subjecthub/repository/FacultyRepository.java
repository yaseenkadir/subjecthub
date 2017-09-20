package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Faculty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends CrudRepository<Faculty, Long>{
    // Haven't verified if these work!
    List<Faculty> findByUniversityId(Long universityId);
    List<Faculty> findByCode(String code);
}
