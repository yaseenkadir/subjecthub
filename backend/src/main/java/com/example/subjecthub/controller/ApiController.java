package com.example.subjecthub.controller;

import com.example.subjecthub.entity.Student;
import com.example.subjecthub.repository.StudentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ApiController {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Returns a list of students.
     *
     * If params are supplied, students matching specified criteria will be returned.
     */
    @RequestMapping(value = "students", method = RequestMethod.GET)
    public List<Student> getStudents(
        @RequestParam(required = false) String faculty,
        @RequestParam(required = false) String courseCode
    ) {
        if (faculty != null && courseCode != null) {
            return studentRepository.findByFacultyAndCourseCode(faculty, courseCode);
        } else if (faculty != null) {
            return studentRepository.findByFaculty(faculty);
        } else if (courseCode != null) {
            return studentRepository.findByCourseCode(courseCode);
        }
        return studentRepository.findAll();
    }

    /**
     * Returns a student with the given id.
     */
    @RequestMapping(value = "students/student/{id}", method = RequestMethod.GET)
    public Student getStudent(
        @PathVariable Long id
    ) {
        return studentRepository.findOne(id);
    }
}
