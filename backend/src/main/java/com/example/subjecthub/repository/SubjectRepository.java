package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subject, Long> {

    List<Subject> findAll();

    List<Subject> findByCodeContainingIgnoreCase(String code);

    List<Subject> findByNameContainingIgnoreCase(String name);

    List<Subject> findByCreditPoints(Integer creditPoints);

    List<Subject> findAllByRatingBetween(double l, double r);
}
