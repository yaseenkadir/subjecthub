package com.example.subjecthub.repository;

import com.example.subjecthub.entity.Student;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.springframework.data.repository.CrudRepository;

@ParametersAreNonnullByDefault
public interface StudentRepository extends CrudRepository<Student, Long> {

    List<Student> findAll();

    List<Student> findByFaculty(String faculty);

    List<Student> findByCourseCode(String courseCode);

    List<Student> findByFacultyAndCourseCode(String faculty, String courseCode);
}
