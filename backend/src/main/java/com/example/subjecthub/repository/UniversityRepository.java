package com.example.subjecthub.repository;

import com.example.subjecthub.entity.University;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UniversityRepository extends CrudRepository<University, Long> {

    public List<University> findAll();

    public List<University> findByAbbreviationLikeIgnoreCaseOrNameLikeIgnoreCase(String abbreviation, String name);
}
