package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Subject;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

  List<Subject> findAll();

  List<Subject> findByCodeContainingIgnoreCase(String code);

  List<Subject> findByNameContainingIgnoreCase(String name);

  List<Subject> findByCreditPoints(Integer creditPoints);

  List<Subject> findAllByRatingBetween(double l, double r);
}
